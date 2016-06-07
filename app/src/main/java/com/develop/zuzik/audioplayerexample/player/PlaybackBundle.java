package com.develop.zuzik.audioplayerexample.player;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.State;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public class PlaybackBundle {

	public final State state;
	public final int currentPositionInMilliseconds;
	public final Integer maxDurationInMilliseconds;
	public final boolean repeat;

	public PlaybackBundle(State state,
						  int currentPositionInMilliseconds,
						  Integer maxDurationInMilliseconds,
						  boolean repeat) {
		this.state = state;
		this.currentPositionInMilliseconds = currentPositionInMilliseconds;
		this.maxDurationInMilliseconds = maxDurationInMilliseconds;
		this.repeat = repeat;
	}

}
