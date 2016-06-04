package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class EndPlayerState implements PlayerState {

	@Override
	public PlaybackBundle getPlaybackBundle() {
		return new PlaybackBundle(PlaybackState.END, 0, null, false);
	}

	@Override
	public void setPlaybackListener(PlaybackListener playbackListener) {
	}

	@Override
	public void setRepeat(boolean repeat) {
	}

	@Override
	public void set() {
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
	public void simulateError() {
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
	}

	@Override
	public void release() {
	}
}
