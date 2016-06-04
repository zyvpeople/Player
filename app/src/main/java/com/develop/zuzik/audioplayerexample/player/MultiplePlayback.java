package com.develop.zuzik.audioplayerexample.player;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.null_objects.NullMultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: handle repeat manually
public class MultiplePlayback {

	private final List<Playback> playbacks = new ArrayList<>();
	private Integer currentPosition;
	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();
	private MultiplePlaybackRepeatMode repeatMode;

	public MultiplePlayback(List<PlayerSource> sources) {
		for (PlayerSource source : sources) {
			this.playbacks.add(new Playback(source));
		}
		if (!sources.isEmpty()) {
			this.currentPosition = 0;
		}
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlaybackBundle getMultiplePlaybackBundle() {
		Playback currentPlaybackOrNull = currentPlaybackOrNull();
		PlaybackBundle currentPlaybackBundle = currentPlaybackOrNull != null
				? currentPlaybackOrNull.getPlaybackBundle()
				: null;
		//TODO: replace to bundle
		PlayerSource currentPlayerSource = currentPlaybackOrNull != null
				? currentPlaybackOrNull.source
				: null;
		return new MultiplePlaybackBundle(
				this.repeatMode,
				currentPlaybackBundle,
				currentPlayerSource,
				playbacksToPlaySources());
	}

	private void currentPlayback(SearchResultListener<Playback> listener) {
		if (this.currentPosition != null) {
			listener.onFound(this.playbacks.get(this.currentPosition));
		}
	}

	private Playback currentPlaybackOrNull() {
		return this.currentPosition != null
				? this.playbacks.get(this.currentPosition)
				: null;
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
					result.setRepeat(true);
					break;
				case REPEAT_ALL:
				case DO_NOT_REPEAT:
					result.setRepeat(false);
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
		playback.setPlaybackListener(bundle -> {
			this.listener.onChange(getMultiplePlaybackBundle());
			if (bundle.state == PlaybackState.COMPLETED
					|| bundle.state == PlaybackState.ERROR
					|| bundle.state == PlaybackState.END) {
				//TODO:also should use shuffle flag and repeat mode
				skipNext(context);
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
			this.currentPosition = nextPlaybackIndex;
			listener.onFound(this.playbacks.get(nextPlaybackIndex));
		});
	}

	private void previousPlayback(SearchResultListener<Playback> listener) {
		currentPlayback(currentPlayback -> {
			int currentPlaybackIndex = this.playbacks.indexOf(currentPlayback);
			int previousPlaybackIndex = (currentPlaybackIndex - 1 + this.playbacks.size()) % this.playbacks.size();
			this.currentPosition = previousPlaybackIndex;
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
