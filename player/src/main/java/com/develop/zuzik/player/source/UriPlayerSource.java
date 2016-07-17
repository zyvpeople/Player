package com.develop.zuzik.player.source;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.RawRes;

import com.develop.zuzik.player.exception.PlayerInitializeException;

import java.io.IOException;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class UriPlayerSource<SourceInfo> implements PlayerSource<SourceInfo> {

	private final SourceInfo sourceInfo;
	@RawRes
	private final Uri uri;

	public UriPlayerSource(SourceInfo sourceInfo, Uri uri) {
		this.sourceInfo = sourceInfo;
		this.uri = uri;
	}

	@Override
	public SourceInfo getSourceInfo() {
		return this.sourceInfo;
	}

	@Override
	public void initializePlayerWithSource(Context context, MediaPlayer player) throws PlayerInitializeException {
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource(context, this.uri);
		} catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
			throw new PlayerInitializeException();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof UriPlayerSource) {
			UriPlayerSource source = (UriPlayerSource) o;
			return this.sourceInfo.equals(source.sourceInfo)
					&& this.uri.equals(source.uri);
		} else {
			return false;
		}
	}
}
