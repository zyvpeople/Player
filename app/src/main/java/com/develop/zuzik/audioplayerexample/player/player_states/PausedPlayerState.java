package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class PausedPlayerState extends BasePlayerState {

	protected PausedPlayerState() {
		super(true, true, player -> {
			int maxDuration = player.getDuration();
			return new PlaybackState(
					State.PAUSED,
					player.getCurrentPosition(),
					maxDuration != -1
							? Optional.of(maxDuration)
							: Optional.absent(),
					player.isLooping());
		});
	}

	@Override
	public final void apply(Context context, PlayerStateContext playerStateContext) {
		super.apply(context, playerStateContext);
		getPlayer(MediaPlayer::pause);
	}

	@Override
	public final void play() {
		super.play();
		setState(new StartedPlayerState());
	}

	@Override
	public final void stop() {
		super.stop();
		stopPlayer();
	}
}
