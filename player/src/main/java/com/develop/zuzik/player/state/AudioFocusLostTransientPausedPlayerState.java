package com.develop.zuzik.player.state;

import com.develop.zuzik.player.state.interfaces.PlayerStateContext;

/**
 * User: zuzik
 * Date: 5/29/16
 */
class AudioFocusLostTransientPausedPlayerState extends PausedPlayerState {

	public AudioFocusLostTransientPausedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext);
	}

	@Override
	public void audioFocusGain() {
		super.audioFocusGain();
		applyState(new StartedPlayerState(this.playerStateContext));
	}
}
