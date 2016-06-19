package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.PlaybackStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ResultAction;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: add logic for add and remove songs from list
public class MultiplePlayback {

	private final List<Playback> playbacks = new ArrayList<>();
	private Optional<Playback> currentPlayback;
	private boolean repeatSingle;
	private boolean shuffle;
	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();
	private final PlaybackStrategyFactory nextPlaybackStrategyFactory;
	private final PlaybackStrategyFactory previousPlaybackStrategyFactory;

	public MultiplePlayback(
			Context context,
			List<PlayerInitializer> initializers,
			PlaybackStrategyFactory nextPlaybackStrategyFactory,
			PlaybackStrategyFactory previousPlaybackStrategyFactory) throws AudioServiceNotSupportException {
		for (PlayerInitializer source : initializers) {
			this.playbacks.add(new Playback(new ContextWrapper(context).getBaseContext(), source));
		}
		this.nextPlaybackStrategyFactory = nextPlaybackStrategyFactory;
		this.previousPlaybackStrategyFactory = previousPlaybackStrategyFactory;
		this.currentPlayback = initializers.isEmpty()
				? Optional.absent()
				: Optional.of(this.playbacks.get(0));
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlaybackState getMultiplePlaybackState() {
		return new MultiplePlaybackState(this.currentPlayback.transform(Playback::getPlayerState), this.repeatSingle, this.shuffle);
	}

	private void currentPlayback(ResultAction<Playback> action) {
		if (this.currentPlayback.isPresent()) {
			action.execute(this.currentPlayback.get());
		}
	}

	public void repeatSingle() {
		this.repeatSingle = true;
		currentPlayback(Playback::repeat);
	}

	public void doNotRepeatSingle() {
		this.repeatSingle = false;
		currentPlayback(Playback::doNotRepeat);
	}

	public void shuffle() {
		this.shuffle = true;
		this.listener.onUpdate();
	}

	public void doNotShuffle() {
		this.shuffle = false;
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

	private void switchFromOldToNewPlayback(Playback oldPlayback, Optional<Playback> newPlayback) {
		if (newPlayback.isPresent()) {
			releasePlayback(oldPlayback);
			this.currentPlayback = newPlayback;
			initPlayback(newPlayback.get(), true);
		}
	}

	private void initPlayback(Playback playback, boolean play) {
		playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate() {
				listener.onUpdate();
				PlaybackState bundle = playback.getPlayerState();
				if (bundle.state == State.COMPLETED) {
					skipNext();
				}
			}

			@Override
			public void onError(Throwable throwable) {
				listener.onError(throwable);
				skipNext();
			}
		});
		playback.init();

		if (this.repeatSingle) {
			playback.repeat();
		} else {
			playback.doNotRepeat();
		}

		if (play) {
			playback.play();
		}
	}

	private void releasePlayback(Playback playback) {
		playback.release();
		playback.setPlaybackListener(null);
	}

	private void nextPlayback(ResultAction<Optional<Playback>> action) {
		currentPlayback(currentPlayback ->
				action.execute(this.nextPlaybackStrategyFactory.create(this.shuffle).determine(this.playbacks, currentPlayback)));
	}

	private void previousPlayback(ResultAction<Optional<Playback>> action) {
		currentPlayback(currentPlayback ->
				action.execute(this.previousPlaybackStrategyFactory.create(this.shuffle).determine(this.playbacks, currentPlayback)));
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(Playback::simulateError);
	}

	//endregion
}
