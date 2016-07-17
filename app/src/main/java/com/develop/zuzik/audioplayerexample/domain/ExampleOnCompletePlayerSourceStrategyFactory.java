package com.develop.zuzik.audioplayerexample.domain;

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedNextPlayerSourceStrategy;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.multipleplayer.player_source_strategy.ShufflePlayerSourceStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExampleOnCompletePlayerSourceStrategyFactory<SourceInfo> implements PlayerSourceStrategyFactory<SourceInfo> {
	@Override
	public PlayerSourceStrategy<SourceInfo> create(boolean shuffle) {
		return shuffle
				? new ShufflePlayerSourceStrategy<>()
				: new EndedNextPlayerSourceStrategy<>();
	}
}
