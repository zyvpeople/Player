package com.develop.zuzik.audioplayerexample.player.playback.interfaces;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public interface PlaybackFactory<SourceInfo> {

	Playback<SourceInfo> create(Context context, PlaybackSettings settings, PlayerSource<SourceInfo> playerSource);
}
