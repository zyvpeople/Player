package com.develop.zuzik.multipleplayer.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;

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
	public void initializedPlayer(ParamAction<MediaPlayer> success) {
		if (this.currentPlayback.isPresent()) {
			this.currentPlayback.get().initializedPlayer(success);
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
	public void addPlayerSource(PlayerSource<SourceInfo> playerSource) {
		//TODO: add
		//TODO: save to state
		//TODO: notify listeners

		//TODO: ? what to do is source already exists
	}

	@Override
	public void removePlayerSource(PlayerSource<SourceInfo> playerSource) {
		//TODO: if this source is current source
		// -> release current source
		// -> remove, save state
		// find next
		// -> if found -> save, init, play
		// else -> save, notify listeners
		//TODO: else remove, save state, notify listeners
	}

	@Override
	public void playPlayerSource(PlayerSource<SourceInfo> playerSource) {
		boolean isCurrentPlayerSource = getMultiplePlaybackState()
				.currentPlaybackState
				.transform(input -> input.playerSource.equals(playerSource))
				.or(false);
		if (isCurrentPlayerSource) {
			return;
		}
		boolean playerSourceExists = getMultiplePlaybackState().playerSources.contains(playerSource);
		if (!playerSourceExists) {
			return;
		}
		releaseCurrentPlaybackAndUpdateState();
		setAndInitAndPlayCurrentPlayback(playerSource);
	}

	@Override
	public void playNextPlayerSource() {
		currentPlayback(
				playback -> {
					Optional<PlayerSource<SourceInfo>> newPlayerSource = this.nextPlayerSourceStrategy.determine(getMultiplePlaybackState().playerSources, playback.getPlaybackState().playerSource);
					if (!newPlayerSource.isPresent()) {
						return;
					}
					releaseCurrentPlaybackAndUpdateState();
					setAndInitAndPlayCurrentPlayback(newPlayerSource.get());
				}, () -> {
					boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
					if (hasPlayerSources) {
						PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
						setAndInitAndPlayCurrentPlayback(playerSource);
					}
				});
	}

	@Override
	public void playPreviousPlayerSource() {
		currentPlayback(
				playback -> {
					Optional<PlayerSource<SourceInfo>> newPlayerSource = this.previousPlayerSourceStrategy.determine(getMultiplePlaybackState().playerSources, playback.getPlaybackState().playerSource);
					if (!newPlayerSource.isPresent()) {
						return;
					}
					releaseCurrentPlaybackAndUpdateState();
					setAndInitAndPlayCurrentPlayback(newPlayerSource.get());
				}, () -> {
					boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
					if (hasPlayerSources) {
						PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
						setAndInitAndPlayCurrentPlayback(playerSource);
					}
				});
	}

	private boolean shouldSwitchToNewPlayerSource(PlaybackState<SourceInfo> playbackState) {
		return playbackState.state == State.COMPLETED;
	}

	private void switchToNewPlayerSource() {
		currentPlayback(
				playback -> {
					Optional<PlayerSource<SourceInfo>> newPlayerSource = this.onCompletePlayerSourceStrategyFactory.create(getMultiplePlaybackState().shuffle).determine(getMultiplePlaybackState().playerSources, playback.getPlaybackState().playerSource);
					if (!newPlayerSource.isPresent()) {
						return;
					}
					releaseCurrentPlaybackAndUpdateState();
					setAndInitAndPlayCurrentPlayback(newPlayerSource.get());
				}, () -> {
					boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
					if (hasPlayerSources) {
						PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
						setAndInitAndPlayCurrentPlayback(playerSource);
					}
				});
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
		currentPlayback(success, () -> {
		});
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
