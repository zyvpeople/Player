package com.develop.zuzik.audioplayerexample.player;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlaybackBundle {

	public final MultiplePlaybackRepeatMode mode;
	public final PlaybackBundle currentPlaybackBundle;
	public final PlayerSource currentPlayerSource;
	public final List<PlayerSource> allSources;

	public MultiplePlaybackBundle(MultiplePlaybackRepeatMode mode,
								  PlaybackBundle currentPlaybackBundle,
								  PlayerSource currentPlayerSource,
								  List<PlayerSource> allSources) {
		this.mode = mode;
		this.currentPlaybackBundle = currentPlaybackBundle;
		this.currentPlayerSource = currentPlayerSource;
		this.allSources = allSources;
	}
}
