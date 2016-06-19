package com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.EndedPreviousPlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.ShufflePlayerSourceStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExamplePreviousPlayerSourceStrategyFactory<SourceInfo> implements PlayerSourceStrategyFactory<SourceInfo> {
	@Override
	public PlayerSourceStrategy<SourceInfo> create(boolean shuffle) {
		return shuffle
				? new ShufflePlayerSourceStrategy()
				: new EndedPreviousPlayerSourceStrategy();
	}
}
