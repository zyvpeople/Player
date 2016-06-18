package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlaybackStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlaybackStrategyFactory {
	PlaybackStrategy create(boolean shuffle);
}
