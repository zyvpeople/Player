package com.develop.zuzik.audioplayerexample.player.playback.null_objects;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public class NullPlaybackListener<SourceInfo> implements PlaybackListener<SourceInfo> {

	@Override
	public void onUpdate(PlaybackState<SourceInfo> playbackState) {
	}

	@Override
	public void onError(Throwable throwable) {
	}
}
