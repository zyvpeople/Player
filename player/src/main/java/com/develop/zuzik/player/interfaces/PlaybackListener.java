package com.develop.zuzik.player.interfaces;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public interface PlaybackListener<SourceInfo> {

	void onUpdate(PlaybackState<SourceInfo> playbackState);

	void onError(Throwable throwable);
}
