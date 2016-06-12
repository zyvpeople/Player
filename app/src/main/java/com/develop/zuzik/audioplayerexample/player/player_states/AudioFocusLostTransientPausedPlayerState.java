package com.develop.zuzik.audioplayerexample.player.player_states;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class AudioFocusLostTransientPausedPlayerState extends PausedPlayerState {

	public AudioFocusLostTransientPausedPlayerState() {
		super();
	}

	@Override
	public void audioFocusGain() {
		super.audioFocusGain();
		setState(new StartedPlayerState());
	}
}
