package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public enum State {
	IDLE,
	PREPARING,
	PLAYING,
	PAUSED,
	ERROR,//TODO:remove Error state
	COMPLETED,
	END,
	NONE
}
