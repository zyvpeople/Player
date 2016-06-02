package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class ErrorPlayerState extends BasePlayerState {

	public ErrorPlayerState(MediaPlayer player, PlayerInitializer initializer, PlayerStateContainer stateContainer) {
		super(player, initializer, stateContainer);
	}
}
