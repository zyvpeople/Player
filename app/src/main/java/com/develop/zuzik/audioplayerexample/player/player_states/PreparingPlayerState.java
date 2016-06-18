package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PreparingPlayerState extends BasePlayerState {

	public PreparingPlayerState() {
		super(false, false, player -> new PlaybackState(
				State.PREPARING,
				0,
				Optional.absent(),
				player.isLooping()));
	}

	@Override
	public void apply(Context context, PlayerStateContext playerStateContext) {
		super.apply(context, playerStateContext);
		getPlayer(value -> {
			value.setOnPreparedListener(preparedPlayer -> setState(new StartedPlayerState()));
			try {
				getPlayerInitializer().initialize(context, value);
				value.prepareAsync();
			} catch (PlayerInitializeException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
				handleError(e);
			}
		});
	}

	@Override
	public void unapply() {
		getPlayer(value -> value.setOnPreparedListener(null));
		super.unapply();
	}
}
