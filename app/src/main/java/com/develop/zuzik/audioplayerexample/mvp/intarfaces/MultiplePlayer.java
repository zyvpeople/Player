package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.RepeatMode;

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

		Observable<Void> onErrorPlayingObservable();

		void play();

		void pause();

		void stop();

		void skipNext();

		void skipPrevious();

		void seekToPosition(int positionInMilliseconds);

		void repeat(RepeatMode repeatMode);

		void shuffle();

		void doNotShuffle();

		void simulateError();
	}

	interface View {

		void enableRepeatMode(boolean doNotRepeat, boolean repeatOne, boolean repeatAll);

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

		void onRepeat(RepeatMode repeatMode);

		void onShuffle();

		void onDoNotShuffle();

		void simulateError();
	}
}
