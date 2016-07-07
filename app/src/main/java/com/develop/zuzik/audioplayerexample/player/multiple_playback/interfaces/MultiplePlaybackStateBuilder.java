package com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlaybackStateBuilder<SourceInfo> {

	private List<PlayerSource<SourceInfo>> playerSources = new ArrayList<>();
	private Optional<PlaybackState<SourceInfo>> currentPlaybackState = Optional.absent();
	private boolean repeatSingle;
	private boolean shuffle;

	MultiplePlaybackStateBuilder(MultiplePlaybackState<SourceInfo> multiplePlaybackState) {
		this.playerSources = multiplePlaybackState.playerSources;
		this.currentPlaybackState = multiplePlaybackState.currentPlaybackState;
		this.repeatSingle = multiplePlaybackState.repeatSingle;
		this.shuffle = multiplePlaybackState.shuffle;
	}

	public MultiplePlaybackStateBuilder<SourceInfo> playerSources(List<PlayerSource<SourceInfo>> playerSources) {
		this.playerSources = playerSources;
		return this;
	}

	public MultiplePlaybackStateBuilder<SourceInfo> currentPlaybackState(Optional<PlaybackState<SourceInfo>> currentPlaybackState) {
		this.currentPlaybackState = currentPlaybackState;
		return this;
	}

	public MultiplePlaybackStateBuilder<SourceInfo> repeatSingle(boolean repeatSingle) {
		this.repeatSingle = repeatSingle;
		return this;
	}

	public MultiplePlaybackStateBuilder<SourceInfo> shuffle(boolean shuffle) {
		this.shuffle = shuffle;
		return this;
	}

	public MultiplePlaybackState<SourceInfo> build() {
		return new MultiplePlaybackState<>(
				new ArrayList<>(this.playerSources),
				this.currentPlaybackState,
				this.repeatSingle,
				this.shuffle);
	}
}
