package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.OnGetMaxDurationListener;
import com.develop.zuzik.audioplayerexample.player.OnSeekListener;
import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	void set(OnGetMaxDurationListener onGetMaxDurationListener, OnSeekListener onSeekListener);

	void unset();

	void play(Context context);

	void pause();

	void stop();

	void fakeError();

	void seekTo(int positionInMilliseconds);
}
