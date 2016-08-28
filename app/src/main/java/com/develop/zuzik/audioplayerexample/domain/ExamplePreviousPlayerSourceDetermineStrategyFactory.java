package com.develop.zuzik.audioplayerexample.domain;

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategy;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategyFactory;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedPreviousPlayerSourceDetermineStrategy;
import com.develop.zuzik.multipleplayer.player_source_strategy.ShufflePlayerSourceDetermineStrategy;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public class ExamplePreviousPlayerSourceDetermineStrategyFactory<SourceInfo> implements PlayerSourceDetermineStrategyFactory<SourceInfo> {
	@Override
	public PlayerSourceDetermineStrategy<SourceInfo> create(boolean shuffle) {
		return shuffle
				? new ShufflePlayerSourceDetermineStrategy<SourceInfo>()
				: new EndedPreviousPlayerSourceDetermineStrategy<>();
	}
}
