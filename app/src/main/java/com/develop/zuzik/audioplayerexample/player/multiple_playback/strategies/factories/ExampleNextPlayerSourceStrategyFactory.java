package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedNextPlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.ShufflePlayerSourceStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExampleNextPlayerSourceStrategyFactory implements PlayerSourceStrategyFactory {
	@Override
	public PlayerSourceStrategy create(boolean shuffle) {
		return shuffle
				? new ShufflePlayerSourceStrategy()
				: new EndedNextPlayerSourceStrategy();
	}
}
