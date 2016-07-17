package com.develop.zuzik.audioplayerexample.player.multiple_playback.player_source_strategies;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class EndedNextPlayerSourceStrategy<SourceInfo> implements PlayerSourceStrategy<SourceInfo> {
	@Override
	public Optional<PlayerSource<SourceInfo>> determine(List<PlayerSource<SourceInfo>> playerSources, PlayerSource<SourceInfo> currentPlayerSource) {
		int currentPlayerSourceIndex = playerSources.indexOf(currentPlayerSource);
		return 0 <= currentPlayerSourceIndex && currentPlayerSourceIndex < playerSources.size() - 1
				? Optional.of(playerSources.get(currentPlayerSourceIndex + 1))
				: Optional.absent();
	}
}
