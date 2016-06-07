package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class EndPlayerState extends BasePlayerState {

	public EndPlayerState() {
		super(false, false);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				State.END,
				0,
				Optional.absent(),
				false);
	}
}
