package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PreparingPlayerState extends BasePlayerState {

	public PreparingPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, false, false);
	}

	@Override
	protected MediaPlayerState playerToState(MediaPlayer player) {
		return new MediaPlayerState(
				State.PREPARING,
				0,
				Optional.absent());
	}

	@Override
	public void apply() throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		super.apply();
		getMediaPlayer().setOnPreparedListener(preparedPlayer -> setState(new StartedPlayerState(this.playerStateContext)));
		getPlayerInitializer().initialize(this.playerStateContext.context(), getMediaPlayer());
		getMediaPlayer().prepareAsync();
		setMediaPlayerState(playerToState(getMediaPlayer()));
	}

	@Override
	public void unapply() {
		getMediaPlayer().setOnPreparedListener(null);
		super.unapply();
	}
}
