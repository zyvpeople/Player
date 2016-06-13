package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedNextPlaybackStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedPreviousPlaybackStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlaybackStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.ShufflePlaybackStrategy;
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
//TODO: create strategyDeterminer to determine strategy because there can be a lot of variation or customer requirements. Also determine strategy when press next or song completed automatically
public class MultiplePlayback {

	private final List<Playback> playbacks = new ArrayList<>();
	private Optional<Integer> currentPosition;
	private boolean repeatSingle;
	private boolean shuffle;
	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();

	public MultiplePlayback(Context context, List<PlayerInitializer> initializers) {
		for (PlayerInitializer source : initializers) {
			this.playbacks.add(new Playback(new ContextWrapper(context).getBaseContext(), source));
		}
		this.currentPosition = initializers.isEmpty()
				? Optional.absent()
				: Optional.of(0);
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlaybackState getMultiplePlaybackState() {
		return new MultiplePlaybackState(currentPlayback().transform(Playback::getState), this.repeatSingle, this.shuffle);
	}

	private void currentPlayback(ResultAction<Playback> action) {
		Optional<Playback> currentPlayback = currentPlayback();
		if (currentPlayback.isPresent()) {
			action.execute(this.currentPosition.transform(this.playbacks::get).get());
		}
	}

	private Optional<Playback> currentPlayback() {
		return this.currentPosition.transform(this.playbacks::get);
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
			updateCurrentPositionForPlayback(newPlayback.get());
			initPlayback(newPlayback.get(), true);
		}
	}

	private void updateCurrentPositionForPlayback(Playback playback) {
		int indexOfPlayback = this.playbacks.indexOf(playback);
		this.currentPosition = indexOfPlayback != -1
				? Optional.of(indexOfPlayback)
				: Optional.absent();
	}

	private void initPlayback(Playback playback, boolean play) {
		playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate() {
				listener.onUpdate();
				PlaybackState bundle = playback.getState();
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
				action.execute(getNextPlaybackStrategy().determine(this.playbacks, currentPlayback)));
	}

	private void previousPlayback(ResultAction<Optional<Playback>> action) {
		currentPlayback(currentPlayback ->
				action.execute(getPreviousPlaybackStrategy().determine(this.playbacks, currentPlayback)));
	}

	private PlaybackStrategy getNextPlaybackStrategy() {
		return determineStrategy(
				new EndedNextPlaybackStrategy(),
				new ShufflePlaybackStrategy());
	}

	private PlaybackStrategy getPreviousPlaybackStrategy() {
		return determineStrategy(
				new EndedPreviousPlaybackStrategy(),
				new ShufflePlaybackStrategy());
	}

	private PlaybackStrategy determineStrategy(PlaybackStrategy defaultStrategy,
											   PlaybackStrategy shuffleStrategy) {
		return this.shuffle
				? shuffleStrategy
				: defaultStrategy;
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(Playback::simulateError);
	}

	//endregion
}
