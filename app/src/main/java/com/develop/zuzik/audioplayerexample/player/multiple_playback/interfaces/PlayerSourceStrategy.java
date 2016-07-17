package com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces;

import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.io.Serializable;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/13/16
 */
//TODO:maybe rename to Determiner
public interface PlayerSourceStrategy<SourceInfo> extends Serializable{
	Optional<PlayerSource<SourceInfo>> determine(
			List<PlayerSource<SourceInfo>> playerSources,
			PlayerSource<SourceInfo> currentPlayerSource);
}
