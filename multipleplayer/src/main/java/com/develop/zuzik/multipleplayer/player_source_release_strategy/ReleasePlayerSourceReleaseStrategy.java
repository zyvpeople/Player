package com.develop.zuzik.multipleplayer.player_source_release_strategy;

import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceReleaseStrategy;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 8/27/16
 */
public class ReleasePlayerSourceReleaseStrategy<SourceInfo> implements PlayerSourceReleaseStrategy<SourceInfo> {
	@Override
	public boolean releaseCurrentPlayback(List<PlayerSource<SourceInfo>> newPlayerSources, PlayerSource<SourceInfo> currentPlayerSource) {
		return true;
	}
}
