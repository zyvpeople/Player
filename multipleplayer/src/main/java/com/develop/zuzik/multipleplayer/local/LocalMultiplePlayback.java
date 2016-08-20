package com.develop.zuzik.multipleplayer.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.multipleplayer.null_object.NullMultiplePlaybackListener;
import com.develop.zuzik.player.broadcast_receiver.PlaybackBroadcastReceiver;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.null_object.NullAction;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class LocalMultiplePlayback<SourceInfo> implements MultiplePlayback<SourceInfo> {

	private final Context context;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy;
	private final PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy;
	private final PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory;
	private MultiplePlaybackState<SourceInfo> multiplePlaybackState;
	private MultiplePlaybackListener<SourceInfo> multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
	private Optional<Playback<SourceInfo>> currentPlayback = Optional.absent();
	private Optional<PlaybackBroadcastReceiver> broadcastReceiver = Optional.absent();

	public LocalMultiplePlayback(Context context,
								 PlaybackFactory<SourceInfo> playbackFactory,
								 PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy,
								 PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy,
								 PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory,
								 boolean repeatSingle,
								 boolean shuffle) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackFactory = playbackFactory;
		this.nextPlayerSourceStrategy = nextPlayerSourceStrategy;
		this.previousPlayerSourceStrategy = previousPlayerSourceStrategy;
		this.onCompletePlayerSourceStrategyFactory = onCompletePlayerSourceStrategyFactory;
		this.multiplePlaybackState = new MultiplePlaybackState<>(
				new ArrayList<>(),
				Optional.absent(),
				repeatSingle,
				shuffle);
	}

	@Override
	public MultiplePlaybackState<SourceInfo> getMultiplePlaybackState() {
		return this.multiplePlaybackState;
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.currentPlayback.isPresent()) {
			this.currentPlayback.get().videoViewSetter(success);
		}
	}

	@Override
	public void setMultiplePlaybackListener(MultiplePlaybackListener<SourceInfo> multiplePlaybackListener) {
		this.multiplePlaybackListener = multiplePlaybackListener != null
				? multiplePlaybackListener
				: NullMultiplePlaybackListener.getInstance();
	}

	@Override
	public void clear() {
		releaseCurrentPlaybackAndUpdateState();
		this.multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
		this.multiplePlaybackState = new MultiplePlaybackState<>(
				new ArrayList<>(),
				Optional.absent(),
				false,
				false);
	}

	@Override
	public void play() {
		currentPlayback(
				Playback::play,
				() -> {
					boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
					if (hasPlayerSources) {
						PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
						setAndInitAndPlayCurrentPlayback(playerSource);
					}
				});
	}

	@Override
	public void pause() {
		currentPlayback(Playback::pause);
	}

	@Override
	public void stop() {
		currentPlayback(Playback::stop);
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
		currentPlayback(playback -> playback.seekTo(positionInMilliseconds));
	}

	@Override
	public void repeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().repeatSingle(true).build();
		currentPlayback(
				Playback::repeat,
				this::notifyStateChanged);
	}

	@Override
	public void doNotRepeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().repeatSingle(false).build();
		currentPlayback(
				Playback::doNotRepeat,
				this::notifyStateChanged);
	}

	@Override
	public void shuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().shuffle(true).build();
		notifyStateChanged();
	}

	@Override
	public void doNotShuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().shuffle(false).build();
		notifyStateChanged();
	}

	@Override
	public void simulateError() {
		currentPlayback(Playback::simulateError);
	}

	@Override
	public void setPlayerSources(List<PlayerSource<SourceInfo>> playerSources) {
		boolean playerSourcesEqual = getMultiplePlaybackState().playerSources.equals(playerSources);
		if (playerSourcesEqual) {
			Log.w(getClass().getSimpleName(), "Attempt to add same player sources");
			return;
		}
		releaseCurrentPlaybackAndUpdateState();
		this.multiplePlaybackState = this.multiplePlaybackState.builder().playerSources(playerSources).build();
		boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
		if (hasPlayerSources) {
			PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
			setAndInitCurrentPlayback(playerSource);
		} else {
			notifyStateChanged();
		}
	}

	@Override
	public void playPlayerSource(PlayerSource<SourceInfo> playerSource) {
		boolean isCurrentPlayerSource = getMultiplePlaybackState()
				.currentPlaybackState
				.transform(input -> input.playerSource.equals(playerSource))
				.or(false);
		if (isCurrentPlayerSource) {
			Log.w(getClass().getSimpleName(), "Attempt to play already played player source");
			return;
		}
		boolean playerSourceExists = getMultiplePlaybackState().playerSources.contains(playerSource);
		if (!playerSourceExists) {
			Log.w(getClass().getSimpleName(), "Attempt to play not existed player source");
			return;
		}
		releaseCurrentPlaybackAndUpdateState();
		setAndInitAndPlayCurrentPlayback(playerSource);
	}

	@Override
	public void playNextPlayerSource() {
		determineAndPlayPlayerSource(this.nextPlayerSourceStrategy);
	}

	@Override
	public void playPreviousPlayerSource() {
		determineAndPlayPlayerSource(this.previousPlayerSourceStrategy);
	}

	private boolean shouldSwitchToNewPlayerSource(PlaybackState<SourceInfo> playbackState) {
		return playbackState.state == State.COMPLETED;
	}

	private void switchToNewPlayerSource() {
		determineAndPlayPlayerSource(this.onCompletePlayerSourceStrategyFactory.create(getMultiplePlaybackState().shuffle));
	}

	private void setAndInitCurrentPlayback(PlayerSource<SourceInfo> playerSource) {
		this.currentPlayback = Optional.of(createPlayback(playerSource));
		currentPlayback(Playback::init);
		this.broadcastReceiver = Optional.of(PlaybackBroadcastReceiver.register(this.context, this.currentPlayback.get()));
	}

	private void setAndInitAndPlayCurrentPlayback(PlayerSource<SourceInfo> playerSource) {
		setAndInitCurrentPlayback(playerSource);
		currentPlayback(Playback::play);
	}

	private void releaseCurrentPlaybackAndUpdateState() {
		currentPlayback(value -> {
			value.release();
			this.currentPlayback = Optional.absent();
			this.multiplePlaybackState.builder().currentPlaybackState(Optional.absent());
		});
		if (this.broadcastReceiver.isPresent()) {
			this.context.unregisterReceiver(this.broadcastReceiver.get());
			this.broadcastReceiver = Optional.absent();
		}
	}

	private void currentPlayback(ParamAction<Playback<SourceInfo>> success) {
		currentPlayback(success, NullAction.INSTANCE);
	}

	private void currentPlayback(ParamAction<Playback<SourceInfo>> success, Action fail) {
		if (this.currentPlayback.isPresent()) {
			success.execute(this.currentPlayback.get());
		} else {
			fail.execute();
		}
	}

	private void notifyStateChanged() {
		this.multiplePlaybackListener.onUpdate(this.multiplePlaybackState);
	}

	private Playback<SourceInfo> createPlayback(PlayerSource<SourceInfo> playerSource) {
		Playback<SourceInfo> playback = this.playbackFactory.create(this.context, this.multiplePlaybackState.repeatSingle, playerSource);
		playback.setPlaybackListener(this.playbackListener);
		if (getMultiplePlaybackState().repeatSingle) {
			playback.repeat();
		} else {
			playback.doNotRepeat();
		}
		return playback;
	}

	private void determineAndPlayPlayerSource(PlayerSourceStrategy<SourceInfo> strategy) {
		currentPlayback(
				playback -> {
					Optional<PlayerSource<SourceInfo>> newPlayerSource = strategy.determine(getMultiplePlaybackState().playerSources, playback.getPlaybackState().playerSource);
					if (!newPlayerSource.isPresent()) {
						Log.w(getClass().getSimpleName(), "Player source is not determined");
						return;
					}
					releaseCurrentPlaybackAndUpdateState();
					setAndInitAndPlayCurrentPlayback(newPlayerSource.get());
				},
				() -> {
					boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
					if (hasPlayerSources) {
						PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
						setAndInitAndPlayCurrentPlayback(playerSource);
					} else {
						Log.w(getClass().getSimpleName(), "There are not player sources to play");
					}
				});
	}

	private final PlaybackListener<SourceInfo> playbackListener = new PlaybackListener<SourceInfo>() {
		@Override
		public void onUpdate(PlaybackState<SourceInfo> playbackState) {
			multiplePlaybackState = multiplePlaybackState
					.builder()
					.currentPlaybackState(Optional.of(playbackState))
					.build();
			notifyStateChanged();
			if (shouldSwitchToNewPlayerSource(playbackState)) {
				switchToNewPlayerSource();
			}
		}

		@Override
		public void onError(Throwable throwable) {
			multiplePlaybackState = multiplePlaybackState
					.builder()
					.currentPlaybackState(currentPlayback.transform(Playback::getPlaybackState))
					.build();
			multiplePlaybackListener.onError(throwable);
			switchToNewPlayerSource();
		}
	};
}
