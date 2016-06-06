package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.null_objects.NullMultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: handle repeat manually
public class MultiplePlayback {

	private final List<Playback> playbacks = new ArrayList<>();
	private Optional<Integer> currentPosition;
	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();
	private MultiplePlaybackRepeatMode repeatMode = MultiplePlaybackRepeatMode.DO_NOT_REPEAT;

	public MultiplePlayback(List<PlayerSource> sources) {
		for (PlayerSource source : sources) {
			this.playbacks.add(new Playback(source));
		}
		this.currentPosition = sources.isEmpty()
				? Optional.absent()
				: Optional.of(0);
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlayerStateBundle getMultiplePlaybackBundle() {
		return new MultiplePlayerStateBundle(
				this.repeatMode,
				currentPlayback().transform(Playback::getPlayerStateBundle),
				currentPlayback().transform(input -> input.source),
				playbacksToPlaySources());
	}

	private void currentPlayback(SearchResultListener<Playback> listener) {
		if (this.currentPosition.isPresent()) {
			listener.onFound(this.currentPosition.transform(this.playbacks::get).get());
		}
	}

	private Optional<Playback> currentPlayback() {
		return this.currentPosition.transform(this.playbacks::get);
	}

	private List<PlayerSource> playbacksToPlaySources() {
		List<PlayerSource> sources = new ArrayList<>();
		for (Playback playback : this.playbacks) {
			sources.add(playback.source);
		}
		return sources;
	}

	public void repeat(MultiplePlaybackRepeatMode mode) {
		this.repeatMode = mode;
		currentPlayback(result -> {
			switch (mode) {
				case REPEAT_ONE:
					result.repeat();
					break;
				case REPEAT_ALL:
				case DO_NOT_REPEAT:
					result.doNotRepeat();
					break;
			}
		});
	}

	public void shuffle() {
		//TODO:
	}

	public void doNotShuffle() {
		//TODO:
	}

	//region Play

	public void init(Context context) {
		currentPlayback(result -> initPlayback(context, result, false));
	}

	public void release() {
		currentPlayback(this::releasePlayback);
	}

	public void play(Context context) {
		currentPlayback(result -> result.play(context));
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

	public void skipNext(Context context) {
		currentPlayback(currentPlayback ->
				nextPlayback(nextPlayback ->
						switchFromOldToNewPlayback(context, currentPlayback, nextPlayback)));
	}

	public void skipPrevious(Context context) {
		currentPlayback(currentPlayback ->
				previousPlayback(previousPlayback ->
						switchFromOldToNewPlayback(context, currentPlayback, previousPlayback)));
	}

	private void switchFromOldToNewPlayback(Context context, Playback oldPlayback, Playback newPlayback) {
		releasePlayback(oldPlayback);
		initPlayback(context, newPlayback, true);
	}

	private void initPlayback(Context context, Playback playback, boolean play) {
		playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onChange() {
				PlayerStateBundle bundle = playback.getPlayerStateBundle();
				listener.onChange(getMultiplePlaybackBundle());
				if (bundle.state == PlaybackState.COMPLETED
						|| bundle.state == PlaybackState.ERROR
						|| bundle.state == PlaybackState.END) {
					//TODO:also should use shuffle flag and repeat mode
					skipNext(context);
				}
			}

			@Override
			public void onError() {
				listener.onError();
			}
		});
		playback.init();
		if (play) {
			playback.play(context);
		}
	}

	private void releasePlayback(Playback playback) {
		playback.setPlaybackListener(null);
		playback.release();
	}

	private void nextPlayback(SearchResultListener<Playback> listener) {
		currentPlayback(currentPlayback -> {
			int currentPlaybackIndex = this.playbacks.indexOf(currentPlayback);
			int nextPlaybackIndex = (currentPlaybackIndex + 1) % this.playbacks.size();
			this.currentPosition = Optional.of(nextPlaybackIndex);
			listener.onFound(this.playbacks.get(nextPlaybackIndex));
		});
	}

	private void previousPlayback(SearchResultListener<Playback> listener) {
		currentPlayback(currentPlayback -> {
			int currentPlaybackIndex = this.playbacks.indexOf(currentPlayback);
			int previousPlaybackIndex = (currentPlaybackIndex - 1 + this.playbacks.size()) % this.playbacks.size();
			this.currentPosition = Optional.of(previousPlaybackIndex);
			listener.onFound(this.playbacks.get(previousPlaybackIndex));
		});
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(Playback::simulateError);
	}

	//endregion

	private interface SearchResultListener<T> {
		void onFound(T result);
	}
}
