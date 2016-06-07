package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class PausedPlayerState extends BasePlayerState {

	public PausedPlayerState() {
		super(true, true);
	}

	@Override
	public PlaybackState getPlayerStateBundle() {
		int maxDuration = getPlayer().getDuration();
		return new PlaybackState(
				State.PAUSED,
				getPlayer().getCurrentPosition(),
				maxDuration != -1
						? Optional.of(maxDuration)
						: Optional.absent(),
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
