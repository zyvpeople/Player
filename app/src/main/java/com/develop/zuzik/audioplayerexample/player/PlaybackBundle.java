package com.develop.zuzik.audioplayerexample.player;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public class PlaybackBundle {

	public final PlaybackState state;
	public final int currentPositionInMilliseconds;
	public final Integer maxDurationInMilliseconds;
	public final boolean repeat;

	public PlaybackBundle(PlaybackState state,
						  int currentPositionInMilliseconds,
						  Integer maxDurationInMilliseconds,
						  boolean repeat) {
		this.state = state;
		this.currentPositionInMilliseconds = currentPositionInMilliseconds;
		this.maxDurationInMilliseconds = maxDurationInMilliseconds;
		this.repeat = repeat;
	}

}
