package com.develop.zuzik.multipleplayer.interfaces;

import com.develop.zuzik.player.source.PlayerSource;

import java.io.Serializable;
import java.util.List;

/**
 * User: zuzik
 * Date: 8/27/16
 */
public interface PlayerSourceReleaseStrategy<SourceInfo> extends Serializable {
	boolean releaseCurrentPlayback(
			List<PlayerSource<SourceInfo>> newPlayerSources,
			PlayerSource<SourceInfo> currentPlayerSource);
}
