package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState() {
		super(true, true, player -> {
			int maxDuration = player.getDuration();
			return new PlaybackState(
					State.COMPLETED,
					maxDuration,
					Optional.of(maxDuration),
					player.isLooping());
		});
	}

	@Override
	public void apply(Context context, PlayerStateContext playerStateContext) {
		super.apply(context, playerStateContext);
		abandonAudioFocus();
	}

	@Override
	public void play() {
		super.play();
		setState(new StartedPlayerState());
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}
}
