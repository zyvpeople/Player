package com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public interface MultiplePlaybackSettings {
	boolean isRepeatSingle();

	void repeatSingle();

	void doNotRepeatSingle();

	boolean isShuffle();

	void shuffle();

	void doNotShuffle();
}
