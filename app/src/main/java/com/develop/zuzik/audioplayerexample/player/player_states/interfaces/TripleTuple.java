package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class TripleTuple<A, B, C> {
	public final A first;
	public final B second;
	public final C third;

	public TripleTuple(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
}
