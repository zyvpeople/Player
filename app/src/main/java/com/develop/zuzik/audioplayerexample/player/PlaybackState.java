package com.develop.zuzik.audioplayerexample.player;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public enum PlaybackState {
	IDLE,
	PREPARING,
	PLAYING,
	PAUSED,
	ERROR,//TODO:remove Error state
	COMPLETED,
	END,
	NONE
}
