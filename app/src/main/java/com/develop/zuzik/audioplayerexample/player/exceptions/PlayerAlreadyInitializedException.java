package com.develop.zuzik.audioplayerexample.player.exceptions;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PlayerAlreadyInitializedException extends Exception {
	public PlayerAlreadyInitializedException() {
		super("Player already initialized");
	}
}
