package com.develop.zuzik.audioplayerexample.player.multiple_playback.null_objects;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class NullMultiplePlaybackListener<SourceInfo> implements MultiplePlaybackListener<SourceInfo> {
	@Override
	public void onUpdate(MultiplePlaybackState<SourceInfo> multiplePlaybackState) {

	}

	@Override
	public void onError(Throwable throwable) {

	}
}
