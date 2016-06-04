package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;

import rx.Observable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model {
		void init();

		void destroy();

		PlaybackBundle getPlaybackState();

		Observable<PlaybackBundle> onPlaybackStateChanged();

		void play(Context context);

		void pause();

		void seekToPosition(int positionInMilliseconds);

		void repeat();

		void doNotRepeat();
	}

	interface View {
		void display(PlaybackBundle bundle, boolean repeat);
	}

	interface Presenter {
		void onInit(View view);

		void onDestroy();

		void onAppear();

		void onDisappear();

		PlaybackBundle getPlaybackState();

		void onPlay(Context context);

		void onPause();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();
	}
}
