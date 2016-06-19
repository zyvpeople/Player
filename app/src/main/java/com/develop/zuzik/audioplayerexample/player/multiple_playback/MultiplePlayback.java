package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: add logic for add and remove songs from list
//TODO: add logic for start any song and not only next and previous
public class MultiplePlayback<SourceInfo> {

	private MultiplePlaybackState<SourceInfo> multiplePlaybackState;
	private Optional<Playback<SourceInfo>> currentPlayback;

	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();
	private final PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory;
	private final PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory;
	private final Context context;

	public MultiplePlayback(
			Context context,
			List<PlayerSource<SourceInfo>> playerSources,
			PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory,
			PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory) throws AudioServiceNotSupportException {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.nextPlayerSourceStrategyFactory = nextPlayerSourceStrategyFactory;
		this.previousPlayerSourceStrategyFactory = previousPlayerSourceStrategyFactory;
		this.currentPlayback = playerSources.isEmpty()
				? Optional.absent()
				: Optional.of(new Playback<SourceInfo>(this.context, playerSources.get(0)));
		this.multiplePlaybackState = new MultiplePlaybackState<SourceInfo>(
				playerSources,
				this.currentPlayback.transform(Playback::getPlaybackState),
				false,
				false);
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlaybackState<SourceInfo> getMultiplePlaybackState() {
		return this.multiplePlaybackState;
	}

	private void currentPlayback(ParamAction<Playback<SourceInfo>> action) {
		if (this.currentPlayback.isPresent()) {
			action.execute(this.currentPlayback.get());
		}
	}

	public void repeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withRepeatSingle(true);
		currentPlayback(Playback::repeat);
	}

	public void doNotRepeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withRepeatSingle(false);
		currentPlayback(Playback::doNotRepeat);
	}

	public void shuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withShuffle(true);
		this.listener.onUpdate();
	}

	public void doNotShuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withShuffle(false);
		this.listener.onUpdate();
	}

	//region Play

	public void init() {
		currentPlayback(result -> initPlayback(result, false));
	}

	public void release() {
		currentPlayback(this::releasePlayback);
	}

	public void play() {
		currentPlayback(Playback::play);
	}

	public void pause() {
		currentPlayback(Playback::pause);
	}

	public void stop() {
		currentPlayback(Playback::stop);
	}

	public void seekTo(int positionInMilliseconds) {
		currentPlayback(result -> result.seekTo(positionInMilliseconds));
	}

	public void skipNext() {
		currentPlayback(currentPlayback ->
				nextPlayback(nextPlayback ->
						switchFromOldToNewPlayback(currentPlayback, nextPlayback)));
	}

	public void skipPrevious() {
		currentPlayback(currentPlayback ->
				previousPlayback(previousPlayback ->
						switchFromOldToNewPlayback(currentPlayback, previousPlayback)));
	}

	private void switchFromOldToNewPlayback(Playback<SourceInfo> oldPlayback, Optional<Playback<SourceInfo>> newPlayback) {
		if (newPlayback.isPresent()) {
			releasePlayback(oldPlayback);
			this.currentPlayback = newPlayback;
			this.multiplePlaybackState = this.multiplePlaybackState.withCurrentPlaybackState(
					this.currentPlayback.transform(Playback::getPlaybackState));
			initPlayback(newPlayback.get(), true);
		}
	}

	private void initPlayback(Playback<SourceInfo> playback, boolean play) {
		playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate() {
				PlaybackState<SourceInfo> bundle = playback.getPlaybackState();
				multiplePlaybackState = multiplePlaybackState.withCurrentPlaybackState(Optional.of(bundle));
				listener.onUpdate();
				if (bundle.state == State.COMPLETED) {
					skipNext();
				}
			}

			@Override
			public void onError(Throwable throwable) {
				multiplePlaybackState = multiplePlaybackState.withCurrentPlaybackState(Optional.of(playback.getPlaybackState()));
				listener.onError(throwable);
				skipNext();
			}
		});
		playback.init();

		if (this.multiplePlaybackState.repeatSingle) {
			playback.repeat();
		} else {
			playback.doNotRepeat();
		}

		if (play) {
			playback.play();
		}
	}

	private void releasePlayback(Playback<SourceInfo> playback) {
		playback.release();
		playback.setPlaybackListener(null);
	}

	private void nextPlayback(ParamAction<Optional<Playback<SourceInfo>>> action) {
		currentPlayback(currentPlayback -> {
			Optional<PlayerSource<SourceInfo>> playerInitializer = this.nextPlayerSourceStrategyFactory
					.create(this.multiplePlaybackState.shuffle)
					.determine(this.multiplePlaybackState.playerSources, currentPlayback.getPlayerSource());
			if (playerInitializer.isPresent()) {
				try {
					action.execute(Optional.of(new Playback<>(this.context, playerInitializer.get())));
				} catch (AudioServiceNotSupportException e) {
					action.execute(Optional.absent());
				}
			} else {
				action.execute(Optional.absent());
			}
		});
	}

	private void previousPlayback(ParamAction<Optional<Playback<SourceInfo>>> action) {
		currentPlayback(currentPlayback -> {
			Optional<PlayerSource<SourceInfo>> playerInitializer = this.previousPlayerSourceStrategyFactory
					.create(this.multiplePlaybackState.shuffle)
					.determine(this.multiplePlaybackState.playerSources, currentPlayback.getPlayerSource());
			if (playerInitializer.isPresent()) {
				try {
					action.execute(Optional.of(new Playback<>(this.context, playerInitializer.get())));
				} catch (AudioServiceNotSupportException e) {
					action.execute(Optional.absent());
				}
			} else {
				action.execute(Optional.absent());
			}
		});
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(Playback::simulateError);
	}

	//endregion
}
