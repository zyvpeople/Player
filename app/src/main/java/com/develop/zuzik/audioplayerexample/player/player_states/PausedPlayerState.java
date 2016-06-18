package com.develop.zuzik.audioplayerexample.player.player_states;

import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.MediaPlayerState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class PausedPlayerState extends BasePlayerState {

	protected PausedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, true);
	}

	@Override
	protected final MediaPlayerState playerToState(MediaPlayer player) {
		int maxDuration = player.getDuration();
		return new MediaPlayerState(
				State.PAUSED,
				player.getCurrentPosition(),
				maxDuration != -1
						? Optional.of(maxDuration)
						: Optional.absent());
	}

	@Override
	public final void apply() {
		super.apply();
		getPlayer(MediaPlayer::pause);
		getPlayer(value -> setMediaPlayerState(playerToState(value)));
	}

	@Override
	public final void play() {
		super.play();
		setState(new StartedPlayerState(this.playerStateContext));
	}

	@Override
	public final void stop() {
		super.stop();
		stopPlayer();
	}
}
