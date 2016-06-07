package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlayerStateContainer;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PreparingPlayerState extends BasePlayerState {

	public PreparingPlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				PlaybackState.PREPARING,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void set(Context context) {
		super.set(context);
		getPlayer().setOnPreparedListener(player -> {
			player.start();
			setState(new StartedPlayerState(player, getSource(), getStateContainer()));
		});
		onPlaybackStateChanged();
		try {
			getSource().initialize(context, getPlayer());
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
