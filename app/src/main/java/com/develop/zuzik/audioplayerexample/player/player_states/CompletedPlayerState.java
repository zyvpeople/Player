package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.PlayerStateContext;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState(PlayerStateContext playerStateContext) {
		super(playerStateContext, true, true, player -> {
			int maxDuration = player.getDuration();
			return new PlaybackState(
					State.COMPLETED,
					maxDuration,
					Optional.of(maxDuration),
					player.isLooping());
		});
	}

	@Override
	public void apply() {
		super.apply();
		abandonAudioFocus();
	}

	@Override
	public void play() {
		super.play();
		setState(new StartedPlayerState(this.playerStateContext));
	}

	@Override
	public void stop() {
		super.stop();
		stopPlayer();
	}
}
