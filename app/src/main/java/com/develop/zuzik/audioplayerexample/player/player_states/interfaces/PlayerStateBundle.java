package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/5/16
 */
public class PlayerStateBundle {

	public final State state;
	public final int currentTimeInMilliseconds;
	public final Optional<Integer> maxTimeInMilliseconds;
	public final boolean repeat;

	public PlayerStateBundle(State state, int currentTimeInMilliseconds, Optional<Integer> maxTimeInMilliseconds, boolean repeat) {
		this.state = state;
		this.currentTimeInMilliseconds = currentTimeInMilliseconds;
		this.maxTimeInMilliseconds = maxTimeInMilliseconds;
		this.repeat = repeat;
	}
}
