package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;

/**
 * User: zuzik
 * Date: 6/7/16
 */
public class PlayerModelState {
	public final PlayerStateBundle bundle;
	public final boolean repeat;

	public PlayerModelState(PlayerStateBundle bundle, boolean repeat) {
		this.bundle = bundle;
		this.repeat = repeat;
	}
}
