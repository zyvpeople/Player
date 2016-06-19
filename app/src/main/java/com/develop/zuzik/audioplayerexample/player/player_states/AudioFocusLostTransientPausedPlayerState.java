package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class AudioFocusLostTransientPausedPlayerState extends PausedPlayerState {

	public AudioFocusLostTransientPausedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext);
	}

	@Override
	public void audioFocusGain() {
		super.audioFocusGain();
		applyState(new StartedPlayerState(this.playerStateContext));
	}
}
