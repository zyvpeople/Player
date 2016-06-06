package com.develop.zuzik.audioplayerexample.player;

import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/5/16
 */
public class PlayerStateBundle {

	public final PlaybackState state;
	public final int currentTimeInMilliseconds;
	public final Optional<Integer> maxTimeInMilliseconds;
	public final boolean repeat;

	public PlayerStateBundle(PlaybackState state, int currentTimeInMilliseconds, Optional<Integer> maxTimeInMilliseconds, boolean repeat) {
		this.state = state;
		this.currentTimeInMilliseconds = currentTimeInMilliseconds;
		this.maxTimeInMilliseconds = maxTimeInMilliseconds;
		this.repeat = repeat;
	}
}
