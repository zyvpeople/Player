package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayerStateBundle;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {
	interface Model {
		void init();

		void destroy();

		MultiplePlayerStateBundle getPlaybackState();

		Observable<MultiplePlayerStateBundle> onPlaybackStateChangedObservable();

		Observable<Void> onErrorPlayingObservable();

		void play();

		void pause();

		void stop();

		void skipNext();

		void skipPrevious();

		void seekToPosition(int positionInMilliseconds);

		void repeat(MultiplePlaybackRepeatMode repeatMode);

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

		MultiplePlayerStateBundle getPlaybackState();

		void onPlay();

		void onPause();

		void onStop();

		void onSkipNext();

		void onSkipPrevious();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat(MultiplePlaybackRepeatMode repeatMode);

		void onShuffle();

		void onDoNotShuffle();

		void simulateError();
	}
}
