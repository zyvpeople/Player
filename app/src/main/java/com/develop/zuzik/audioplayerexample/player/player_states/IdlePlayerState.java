package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class IdlePlayerState extends BasePlayerState {

	public IdlePlayerState() {
		super(true, false);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				State.IDLE,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void play() {
		super.play();
		getPlayer().reset();
		setState(new PreparingPlayerState());
	}
}
