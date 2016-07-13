package com.develop.zuzik.audioplayerexample.player.playback.local;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

/**
 * User: zuzik
 * Date: 6/23/16
 */
public class LocalPlaybackFactory<SourceInfo> implements PlaybackFactory<SourceInfo> {
	@Override
	public Playback<SourceInfo> create(Context context, boolean repeat, PlayerSource<SourceInfo> playerSource) {
		return new LocalPlayback<>(context, repeat, playerSource);
	}
}
