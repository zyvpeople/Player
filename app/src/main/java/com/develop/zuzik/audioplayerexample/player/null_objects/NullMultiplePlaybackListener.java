package com.develop.zuzik.audioplayerexample.player.null_objects;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.MultiplePlaybackListener;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class NullMultiplePlaybackListener implements MultiplePlaybackListener {
	@Override
	public void onChange(MultiplePlayerStateBundle bundle) {
	}

	@Override
	public void onError() {
	}
}
