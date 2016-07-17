package com.develop.zuzik.player.local;

import android.content.Context;

import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.source.PlayerSource;

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
