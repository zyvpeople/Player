package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies;

import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public interface PlaybackStrategy {
	Optional<Playback> determine(List<Playback> playbacks, Playback currentPlayback);
}
