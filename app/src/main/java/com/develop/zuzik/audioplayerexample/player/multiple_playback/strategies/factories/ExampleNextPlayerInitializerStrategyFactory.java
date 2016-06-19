package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedNextPlayerInitializerStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlayerInitializerStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.ShufflePlayerInitializerStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExampleNextPlayerInitializerStrategyFactory implements PlayerInitializerStrategyFactory {
	@Override
	public PlayerInitializerStrategy create(boolean shuffle) {
		return shuffle
				? new ShufflePlayerInitializerStrategy()
				: new EndedNextPlayerInitializerStrategy();
	}
}
