package com.develop.zuzik.audioplayerexample.player.player_states;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 5/29/16
 */
abstract class PausedPlayerState extends BasePlayerState {

	protected PausedPlayerState() {
		super(true, true);
	}

	@Override
	public final PlaybackState getPlaybackState() {
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
	public final void play() {
		super.play();
		startPlayer();
	}

	@Override
	public final void stop() {
		super.stop();
		stopPlayer();
	}
}
