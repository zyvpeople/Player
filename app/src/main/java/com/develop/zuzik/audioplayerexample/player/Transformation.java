package com.develop.zuzik.audioplayerexample.player;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public interface Transformation<From, To> {
	To transform(From from);
}
