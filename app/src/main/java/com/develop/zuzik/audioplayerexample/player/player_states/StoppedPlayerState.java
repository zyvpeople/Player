package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class StoppedPlayerState extends BasePlayerState {

	public StoppedPlayerState(MediaPlayer player, PlayerStateContainer stateContainer) {
		super(player, stateContainer);
	}

	@Override
	public void stop(Context context) {
		super.stop(context);
		getPlayer().stop();
	}
}
