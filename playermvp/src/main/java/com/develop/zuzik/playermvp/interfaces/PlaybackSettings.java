package com.develop.zuzik.playermvp.interfaces;

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
