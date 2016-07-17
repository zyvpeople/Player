package com.develop.zuzik.player.interfaces;

import android.content.Context;

import com.develop.zuzik.player.source.PlayerSource;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public interface PlaybackFactory<SourceInfo> extends Serializable {

	Playback<SourceInfo> create(Context context, boolean repeat, PlayerSource<SourceInfo> playerSource);
}
