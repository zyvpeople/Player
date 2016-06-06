package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.MultiplePlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;

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

		Observable<MultiplePlayerStateBundle> onPlaybackStateChanged();

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
		void display(MultiplePlayerStateBundle bundle, MultiplePlaybackRepeatMode repeatMode);
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
