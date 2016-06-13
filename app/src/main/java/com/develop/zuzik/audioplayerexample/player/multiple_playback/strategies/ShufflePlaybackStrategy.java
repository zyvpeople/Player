package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;
import java.util.Random;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class ShufflePlaybackStrategy implements PlaybackStrategy {
	@Override
	public Optional<Playback> determine(List<Playback> playbacks, Playback currentPlayback) {
		int currentPlaybackIndex = playbacks.indexOf(currentPlayback);
		return currentPlaybackIndex >= 0
				? Optional.of(playbacks.get(new Random().nextInt(Integer.MAX_VALUE) % playbacks.size()))
				: Optional.absent();
	}
}
