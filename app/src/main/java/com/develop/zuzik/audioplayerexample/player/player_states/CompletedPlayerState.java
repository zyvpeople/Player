package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class CompletedPlayerState extends BasePlayerState {

	public CompletedPlayerState() {
		super(true, true);
	}

	@Override
	public PlaybackState getPlaybackState() {
		int maxDuration = getPlayer().getDuration();
		return new PlaybackState(
				State.COMPLETED,
				maxDuration,
				Optional.of(maxDuration),
				getPlayer().isLooping());
	}

	@Override
	public void play() {
		super.play();
		getPlayer().start();
		setState(new StartedPlayerState());
	}

	@Override
	public void stop() {
		super.stop();
		getPlayer().stop();
		getPlayer().reset();
		setState(new IdlePlayerState());
	}
}
