package com.develop.zuzik.multipleplayermvp.interfaces;

import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface ControlAvailabilityStrategy<SourceInfo> {
	boolean available(
			List<PlayerSource<SourceInfo>> playerSources,
			PlayerSource<SourceInfo> currentPlayerSource,
			boolean shuffle);
}
