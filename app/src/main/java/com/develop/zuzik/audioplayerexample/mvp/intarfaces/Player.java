package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model {
		void init();

		void destroy();

		PlayerModelState getState();

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

	interface View {
		void setRepeat();

		void setDoNotRepeat();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showProgress();

		void hideProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void showLoading();

		void hideLoading();
	}

	interface Presenter {
		void onInit(View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onPlay();

		void onPause();

		void onStop();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void simulateError();
	}
}
