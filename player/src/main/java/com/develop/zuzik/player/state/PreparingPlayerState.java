package com.develop.zuzik.player.state;

import android.media.MediaPlayer;

import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.state.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
class PreparingPlayerState extends BasePlayerState {

	public PreparingPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, false, false);
	}

	@Override
	protected MediaPlayerState playerToState(MediaPlayer player) {
		return new MediaPlayerState(
				State.PREPARING,
				0,
				Optional.<Integer>absent());
	}

	@Override
	protected void doOnApply(MediaPlayer player) throws IllegalStateException, PlayerInitializeException, FailRequestAudioFocusException {
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				applyState(new StartedPlayerState(playerStateContext));
			}
		});
		getPlayerInitializer().initializePlayerWithSource(this.playerStateContext.context(), player);
		getMediaPlayer().prepareAsync();
	}

	@Override
	public void unapply() {
		getMediaPlayer().setOnPreparedListener(null);
		super.unapply();
	}
}
