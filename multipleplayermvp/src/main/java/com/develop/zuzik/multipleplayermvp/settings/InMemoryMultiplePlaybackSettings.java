package com.develop.zuzik.multipleplayermvp.settings;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlaybackSettings;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public class InMemoryMultiplePlaybackSettings implements MultiplePlaybackSettings {

	private boolean repeatSingle;
	private boolean shuffle;

	@Override
	public boolean isRepeatSingle() {
		return this.repeatSingle;
	}

	@Override
	public void repeatSingle() {
		this.repeatSingle = true;
	}

	@Override
	public void doNotRepeatSingle() {
		this.repeatSingle = false;
	}

	@Override
	public boolean isShuffle() {
		return this.shuffle;
	}

	@Override
	public void shuffle() {
		this.shuffle = true;
	}

	@Override
	public void doNotShuffle() {
		this.shuffle = false;
	}
}
