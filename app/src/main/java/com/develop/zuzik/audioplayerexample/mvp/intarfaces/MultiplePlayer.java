package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {
	interface Model {
		void init();

		void destroy();

		MultiplePlayerModelState getState();

		Observable<Void> stateChangedObservable();

		Observable<Throwable> errorPlayingObservable();

		void play();

		void pause();

		void stop();

		void skipNext();

		void skipPrevious();

		void seekToPosition(int positionInMilliseconds);

		void repeatSingle();

		void doNotRepeatSingle();

		void shuffle();

		void doNotShuffle();

		void simulateError();
	}

	interface View {
		void repeat();

		void doNotRepeat();

		void shuffle();

		void doNotShuffle();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showProgress();

		void hideProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);
	}

	interface Presenter {
		void onInit(View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onPlay();

		void onPause();

		void onStop();

		void onSkipNext();

		void onSkipPrevious();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeatSingle();

		void onDoNotRepeatSingle();

		void onShuffle();

		void onDoNotShuffle();

		void onRepeatAll();

		void onDoNotRepeatAll();

		void simulateError();
	}
}
