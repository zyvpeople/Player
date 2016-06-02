package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	void set();

	void unset();

	void play(Context context);

	void pause();

	void stop();
}
