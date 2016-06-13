package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class NextPlaybackStrategy implements PlaybackStrategy {
	@Override
	public Optional<Playback> determine(List<Playback> playbacks, Playback currentPlayback) {
		int currentPlaybackIndex = playbacks.indexOf(currentPlayback);
		return 0 <= currentPlaybackIndex && currentPlaybackIndex < playbacks.size() - 1
				? Optional.of(playbacks.get(currentPlaybackIndex + 1))
				: Optional.absent();
	}
}
