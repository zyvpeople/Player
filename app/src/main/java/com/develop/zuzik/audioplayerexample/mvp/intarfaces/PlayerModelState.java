package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;

/**
 * User: zuzik
 * Date: 6/7/16
 */
public class PlayerModelState {
	public final PlaybackState bundle;
	public final boolean repeat;

	public PlayerModelState(PlaybackState bundle, boolean repeat) {
		this.bundle = bundle;
		this.repeat = repeat;
	}
}
