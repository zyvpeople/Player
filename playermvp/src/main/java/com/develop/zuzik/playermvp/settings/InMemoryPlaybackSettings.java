package com.develop.zuzik.playermvp.settings;

import com.develop.zuzik.playermvp.interfaces.PlaybackSettings;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public class InMemoryPlaybackSettings implements PlaybackSettings {
	private boolean repeat;

	@Override
	public boolean isRepeat() {
		return this.repeat;
	}

	@Override
	public void repeat() {
		this.repeat = true;
	}

	@Override
	public void doNotRepeat() {
		this.repeat = false;
	}
}
