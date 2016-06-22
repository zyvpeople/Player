package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model<SourceInfo> {
		void initWithSource(PlayerSource<SourceInfo> source);

		void destroy();

		Optional<PlaybackState<SourceInfo>> getState();

		Observable<Void> stateChangedObservable();

		Observable<Throwable> errorPlayingObservable();

		void play();

		void pause();

		void stop();

		void seekToPosition(int positionInMilliseconds);

		void repeat();

		void doNotRepeat();

		void simulateError();
	}

	interface View<SourceInfo> {
		void setRepeat();

		void setDoNotRepeat();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showSourceProgress();

		void hideSourceProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void showLoadingIndicator();

		void hideLoadingIndicator();

		void displayCurrentSource(SourceInfo sourceInfo);

		void doNotDisplayCurrentSource();

	}

	interface Presenter<SourceInfo> {
		void setView(View<SourceInfo> view);

		void onCreated();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onSetSource(PlayerSource<SourceInfo> source);

		void onPlay();

		void onPause();

		void onStop();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void simulateError();
	}
}
