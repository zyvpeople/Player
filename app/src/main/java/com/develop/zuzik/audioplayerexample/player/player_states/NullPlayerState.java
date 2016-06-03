package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class NullPlayerState implements PlayerState {

	@Override
	public PlaybackBundle getPlaybackBundle() {
		return new PlaybackBundle(PlaybackState.NONE, 0, null);
	}

	@Override
	public void setPlaybackListener(PlaybackListener playbackListener) {
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
	public void fakeError() {
	}

	@Override
	public void seekTo(int positionInMilliseconds) {
	}

	@Override
	public void release() {
	}
}
