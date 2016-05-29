package com.develop.zuzik.audioplayerexample.player.player_sources;

import android.support.annotation.RawRes;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class RawResourcePlayerSource implements PlayerSource {
	@RawRes
	public final int rawResId;

	public RawResourcePlayerSource(int rawResId) {
		this.rawResId = rawResId;
	}
}
