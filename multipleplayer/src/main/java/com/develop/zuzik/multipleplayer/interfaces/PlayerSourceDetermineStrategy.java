package com.develop.zuzik.multipleplayer.interfaces;

import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.io.Serializable;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
public interface PlayerSourceDetermineStrategy<SourceInfo> extends Serializable{
	Optional<PlayerSource<SourceInfo>> determine(
			List<PlayerSource<SourceInfo>> playerSources,
			PlayerSource<SourceInfo> currentPlayerSource);
}
