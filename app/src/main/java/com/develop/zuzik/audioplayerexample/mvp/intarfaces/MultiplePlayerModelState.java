package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;

/**
 * User: zuzik
 * Date: 6/7/16
 */
public class MultiplePlayerModelState<SourceInfo> {
	public final MultiplePlaybackState<SourceInfo> bundle;
	public final boolean repeat;
	public final boolean shuffle;

	public MultiplePlayerModelState(MultiplePlaybackState<SourceInfo> bundle, boolean repeat, boolean shuffle) {
		this.bundle = bundle;
		this.repeat = repeat;
		this.shuffle = shuffle;
	}
}
