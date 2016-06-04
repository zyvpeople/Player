package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {
	interface Model {
		void init();

		void destroy();

		MultiplePlaybackBundle getPlaybackState();

		Observable<MultiplePlaybackBundle> onPlaybackStateChanged();

		void play(Context context);

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
		void display(MultiplePlaybackBundle bundle, MultiplePlaybackRepeatMode repeatMode);
	}

	interface Presenter {
		void onInit(View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		MultiplePlaybackBundle getPlaybackState();

		void onPlay(Context context);

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
