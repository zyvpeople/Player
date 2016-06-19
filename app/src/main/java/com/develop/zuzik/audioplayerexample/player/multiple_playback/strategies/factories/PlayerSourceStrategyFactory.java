package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlayerSourceStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlayerSourceStrategyFactory {
	PlayerSourceStrategy create(boolean shuffle);
}
