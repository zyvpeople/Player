package com.develop.zuzik.audioplayerexample.player.exceptions;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class AudioServiceNotSupportException extends Exception {
	public AudioServiceNotSupportException() {
		super("Audio service is not exist on this device");
	}
}
