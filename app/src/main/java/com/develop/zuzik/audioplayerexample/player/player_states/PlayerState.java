package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackListener;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	void setPlaybackListener(PlaybackListener playbackListener);

	void set();

	void unset();

	void play(Context context);

	void pause();

	void stop();

	void fakeError();

	void seekTo(int positionInMilliseconds);

	void release();
}
