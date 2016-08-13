package com.develop.zuzik.player.interfaces;

import android.media.MediaPlayer;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public interface Playback<SourceInfo> {

	PlaybackState<SourceInfo> getPlaybackState();

	//TODO: do not use mediaPlayer. create some class to configure view
	void initializedPlayer(ParamAction<MediaPlayer> success);

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