package com.develop.zuzik.audioplayerexample.player.playback;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public interface PlaybackSettings extends Serializable{
	boolean isRepeat();

	void repeat();

	void doNotRepeat();
}
