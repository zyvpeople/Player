package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public interface Transformation<From, To> {
	To transform(From from);
}
