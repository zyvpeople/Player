package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateBundle;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PreparingPlayerState extends BasePlayerState {

	public PreparingPlayerState() {
		super(false, false);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				State.PREPARING,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void set(Context context) {
		super.set(context);
		getPlayer().setOnPreparedListener(player -> {
			player.start();
			setState(new StartedPlayerState());
		});
		try {
			getPlayerSource().initialize(context, getPlayer());
			getPlayer().prepareAsync();
		} catch (PlayerInitializeException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
			handleError();
		}
	}

	@Override
	public void unset() {
		getPlayer().setOnPreparedListener(null);
		super.unset();
	}
}
