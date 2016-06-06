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
public class IdlePlayerState extends BasePlayerState {

	private boolean preparing;

	public IdlePlayerState(MediaPlayer player, PlayerSource source, PlayerStateContainer stateContainer) {
		super(player, source, stateContainer);
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return new PlayerStateBundle(
				this.preparing ? PlaybackState.PREPARING : PlaybackState.IDLE,
				0,
				Optional.absent(),
				getPlayer().isLooping());
	}

	@Override
	public void setRepeat(boolean repeat) {
		if (!this.preparing) {
			getPlayer().setLooping(repeat);
		}
	}

	@Override
	public void set() {
		super.set();
		getPlayer().setOnPreparedListener(player -> {
			player.start();
			setState(new StartedPlayerState(player, getSource(), getStateContainer()));
		});
		onPlaybackStateChanged();
	}

	@Override
	public void unset() {
		getPlayer().setOnPreparedListener(null);
		super.unset();
	}

	@Override
	public void play(Context context) {
		super.play(context);
		if (this.preparing) {
			return;
		}
		this.preparing = true;
		getPlayer().reset();
		try {
			onPlaybackStateChanged();
			getSource().initialize(context, getPlayer());
			getPlayer().prepareAsync();
		} catch (PlayerInitializeException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
			handleError();
		}
	}
}
