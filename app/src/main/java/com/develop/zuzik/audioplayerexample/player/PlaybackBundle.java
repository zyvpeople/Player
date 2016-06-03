package com.develop.zuzik.audioplayerexample.player;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public class PlaybackBundle {

	public final PlaybackState state;
	public final int currentPositionInMilliseconds;
	public final Integer maxDurationInMilliseconds;

	public PlaybackBundle(PlaybackState state,
						  int currentPositionInMilliseconds,
						  Integer maxDurationInMilliseconds) {
		this.state = state;
		this.currentPositionInMilliseconds = currentPositionInMilliseconds;
		this.maxDurationInMilliseconds = maxDurationInMilliseconds;
	}

}
