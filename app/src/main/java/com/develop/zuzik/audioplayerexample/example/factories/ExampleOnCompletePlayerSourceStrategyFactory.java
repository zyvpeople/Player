package com.develop.zuzik.audioplayerexample.example.factories;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.player_source_strategies.EndedNextPlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.player_source_strategies.ShufflePlayerSourceStrategy;

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
