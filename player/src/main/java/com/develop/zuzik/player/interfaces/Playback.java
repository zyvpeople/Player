package com.develop.zuzik.player.interfaces;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public interface Playback<SourceInfo> {

	PlaybackState<SourceInfo> getPlaybackState();

	void videoViewSetter(ParamAction<VideoViewSetter> success);

	void setPlaybackListener(PlaybackListener<SourceInfo> playbackListener);

	void init();

	void release();

	void play();

	void pause();

	void stop();

	void seekTo(int positionInMilliseconds);

	void repeat();

	void doNotRepeat();

	void simulateError();

}