package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.OnGetMaxDurationListener;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class NullPlayerState implements PlayerState {

	@Override
	public void set(OnGetMaxDurationListener onGetMaxDurationListener) {
	}

	@Override
	public void unset() {
	}

	@Override
	public void play(Context context) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void fakeError() {
	}
}
