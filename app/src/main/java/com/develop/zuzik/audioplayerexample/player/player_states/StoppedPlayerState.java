package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class StoppedPlayerState extends PlayerState {

	public StoppedPlayerState(MediaPlayer player, PlayerStateContainer stateContainer) {
		super(player, stateContainer);
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
	}
}
