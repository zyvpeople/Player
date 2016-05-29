package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class InitializedPlayerState extends PlayerState {

	public InitializedPlayerState(MediaPlayer player, PlayerStateContainer stateContainer) {
		super(player, stateContainer);
	}
}
