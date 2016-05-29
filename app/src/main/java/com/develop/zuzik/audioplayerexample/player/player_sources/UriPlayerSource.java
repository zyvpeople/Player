package com.develop.zuzik.audioplayerexample.player.player_sources;

import android.net.Uri;
import android.support.annotation.RawRes;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class UriPlayerSource implements PlayerSource {
	@RawRes
	public final Uri uri;

	public UriPlayerSource(Uri uri) {
		this.uri = uri;
	}
}
