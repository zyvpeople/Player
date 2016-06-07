package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.RepeatMode;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayerStateBundle;

/**
 * User: zuzik
 * Date: 6/7/16
 */
public class MultiplePlayerModelState {
	public final MultiplePlayerStateBundle bundle;
	public final RepeatMode repeat;

	public MultiplePlayerModelState(MultiplePlayerStateBundle bundle, RepeatMode repeat) {
		this.bundle = bundle;
		this.repeat = repeat;
	}
}
