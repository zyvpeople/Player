package com.develop.zuzik.multipleplayer.player_source_strategy;

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class CyclicPreviousPlayerSourceStrategy<SourceInfo> implements PlayerSourceStrategy<SourceInfo> {
	@Override
	public Optional<PlayerSource<SourceInfo>> determine(List<PlayerSource<SourceInfo>> playerSources, PlayerSource<SourceInfo> currentPlayerSource) {
		int currentPlayerSourceIndex = playerSources.indexOf(currentPlayerSource);
		return currentPlayerSourceIndex >= 0
				? Optional.of(playerSources.get((currentPlayerSourceIndex - 1 + playerSources.size()) % playerSources.size()))
				: Optional.<PlayerSource<SourceInfo>>absent();
	}
}
