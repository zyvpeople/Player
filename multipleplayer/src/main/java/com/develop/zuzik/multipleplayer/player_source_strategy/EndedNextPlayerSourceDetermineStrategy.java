package com.develop.zuzik.multipleplayer.player_source_strategy;

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategy;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public class EndedNextPlayerSourceDetermineStrategy<SourceInfo> implements PlayerSourceDetermineStrategy<SourceInfo> {
	@Override
	public Optional<PlayerSource<SourceInfo>> determine(List<PlayerSource<SourceInfo>> playerSources, PlayerSource<SourceInfo> currentPlayerSource) {
		int currentPlayerSourceIndex = playerSources.indexOf(currentPlayerSource);
		return 0 <= currentPlayerSourceIndex && currentPlayerSourceIndex < playerSources.size() - 1
				? Optional.of(playerSources.get(currentPlayerSourceIndex + 1))
				: Optional.<PlayerSource<SourceInfo>>absent();
	}
}
