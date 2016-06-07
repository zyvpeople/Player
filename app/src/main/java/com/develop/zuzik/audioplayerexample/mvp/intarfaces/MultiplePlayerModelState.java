package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayerStateBundle;

/**
 * User: zuzik
 * Date: 6/7/16
 */
public class MultiplePlayerModelState {
	public final MultiplePlayerStateBundle bundle;
	public final MultiplePlaybackRepeatMode repeat;

	public MultiplePlayerModelState(MultiplePlayerStateBundle bundle, MultiplePlaybackRepeatMode repeat) {
		this.bundle = bundle;
		this.repeat = repeat;
	}
}
