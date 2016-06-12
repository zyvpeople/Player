package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayback {

	private final List<Playback> playbacks = new ArrayList<>();
	private Optional<Integer> currentPosition;
	private boolean repeat;
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
		return new MultiplePlaybackState(currentPlayback().transform(Playback::getState), this.repeat);
	}

	private void currentPlayback(SearchResultListener<Playback> listener) {
		Optional<Playback> currentPlayback = currentPlayback();
		if (currentPlayback.isPresent()) {
			listener.onFound(this.currentPosition.transform(this.playbacks::get).get());
		}
	}

	private Optional<Playback> currentPlayback() {
		return this.currentPosition.transform(this.playbacks::get);
	}

	public void repeat() {
		this.repeat = true;
		currentPlayback(playback -> playback.repeat());
	}

	public void doNotRepeat() {
		this.repeat = false;
		currentPlayback(playback -> playback.doNotRepeat());
	}

	public void shuffle() {
		//TODO:
	}

	public void doNotShuffle() {
		//TODO:
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

	private void switchFromOldToNewPlayback(Playback oldPlayback, Playback newPlayback) {
		updateCurrentPositionForPlayback(newPlayback);
		releasePlayback(oldPlayback);
		initPlayback(newPlayback, true);
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

		if (this.repeat) {
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

	private void nextPlayback(SearchResultListener<Playback> listener) {
		currentPlayback(currentPlayback -> {
			int currentPlaybackIndex = this.playbacks.indexOf(currentPlayback);
			int nextPlaybackIndex = (currentPlaybackIndex + 1) % this.playbacks.size();
			listener.onFound(this.playbacks.get(nextPlaybackIndex));
		});
	}

	private void previousPlayback(SearchResultListener<Playback> listener) {
		currentPlayback(currentPlayback -> {
			int currentPlaybackIndex = this.playbacks.indexOf(currentPlayback);
			int previousPlaybackIndex = (currentPlaybackIndex - 1 + this.playbacks.size()) % this.playbacks.size();
			listener.onFound(this.playbacks.get(previousPlaybackIndex));
		});
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(playback -> playback.simulateError());
	}

	//endregion

	private interface SearchResultListener<T> {
		void onFound(T result);
	}
}
