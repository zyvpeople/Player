package com.develop.zuzik.audioplayerexample.player.playback;

/**
 * User: zuzik
 * Date: 6/3/16
 */
public interface PlaybackListener {

	void onUpdate();

	void onError(Throwable throwable);
}
