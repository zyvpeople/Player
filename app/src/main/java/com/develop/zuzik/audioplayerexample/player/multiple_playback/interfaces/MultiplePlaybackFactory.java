package com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces;

import android.content.Context;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public interface MultiplePlaybackFactory<SourceInfo> {

	MultiplePlayback<SourceInfo> create(Context context);
}
