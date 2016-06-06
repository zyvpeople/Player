package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model {
		void init();

		void destroy();

		PlayerStateBundle getPlayerStateBundle();

		Observable<Void> onPlayerStateChangedObservable();

		void play(Context context);

		void pause();

		void stop();

		void seekToPosition(int positionInMilliseconds);

		void repeat();

		void doNotRepeat();

		void simulateError();
	}

	interface View {
		void display(PlayerStateBundle bundle, boolean repeat);
	}

	interface Presenter {
		void onInit(View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onPlay(Context context);

		void onPause();

		void onStop();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void simulateError();
	}
}
