package com.develop.zuzik.multipleplayer.interfaces;

import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.io.Serializable;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlaybackState<SourceInfo> implements Serializable {

	public final List<PlayerSource<SourceInfo>> playerSources;
	public final Optional<PlaybackState<SourceInfo>> currentPlaybackState;
	public final boolean repeatSingle;
	public final boolean shuffle;

	public MultiplePlaybackState(
			List<PlayerSource<SourceInfo>> playerSources,
			Optional<PlaybackState<SourceInfo>> currentPlaybackState,
			boolean repeatSingle,
			boolean shuffle) {
		this.playerSources = playerSources;
		this.currentPlaybackState = currentPlaybackState;
		this.repeatSingle = repeatSingle;
		this.shuffle = shuffle;
	}

	public MultiplePlaybackStateBuilder<SourceInfo> builder() {
		return new MultiplePlaybackStateBuilder<>(this);
	}
}
