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
		void init(Context context);

		void destroy();

		MultiplePlayerStateBundle getPlaybackState();

		Observable<MultiplePlayerStateBundle> onPlaybackStateChangedObservable();

		Observable<Void> onErrorPlayingObservable();

		void play(Context context);

		void pause();

		void stop();

		void skipNext(Context context);

		void skipPrevious(Context context);

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
		void onInit(Context context, View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		MultiplePlayerStateBundle getPlaybackState();

		void onPlay(Context context);

		void onPause();

		void onStop();

		void onSkipNext(Context context);

		void onSkipPrevious(Context context);

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat(MultiplePlaybackRepeatMode repeatMode);

		void onShuffle();

		void onDoNotShuffle();

		void simulateError();
	}
}
