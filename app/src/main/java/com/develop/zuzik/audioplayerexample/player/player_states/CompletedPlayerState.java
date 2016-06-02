package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState(MediaPlayer player, PlayerInitializer initializer, PlayerStateContainer stateContainer) {
		super(player, initializer, stateContainer);
	}

	@Override
	public void play(Context context) {
		super.play(context);
		getPlayer().start();
		setState(new StartedPlayerState(getPlayer(), getInitializer(), getStateContainer()));
	}
}
