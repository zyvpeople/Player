package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlayerInitializerStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlayerInitializerStrategyFactory {
	PlayerInitializerStrategy create(boolean shuffle);
}
