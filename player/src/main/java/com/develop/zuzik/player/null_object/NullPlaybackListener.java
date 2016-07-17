package com.develop.zuzik.player.null_object;

import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public class NullPlaybackListener<SourceInfo> implements PlaybackListener<SourceInfo> {

	private static final NullPlaybackListener INSTANCE = new NullPlaybackListener();

	public static <SourceInfo> NullPlaybackListener<SourceInfo> getInstance() {
		return INSTANCE;
	}

	private NullPlaybackListener() {
	}

	@Override
	public void onUpdate(PlaybackState<SourceInfo> playbackState) {
	}

	@Override
	public void onError(Throwable throwable) {
	}
}
