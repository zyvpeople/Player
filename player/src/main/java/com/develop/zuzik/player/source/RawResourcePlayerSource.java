package com.develop.zuzik.player.source;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;
import android.util.Log;

import com.develop.zuzik.player.exception.PlayerInitializeException;

import java.io.IOException;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class RawResourcePlayerSource<SourceInfo> implements PlayerSource<SourceInfo> {

	private final SourceInfo sourceInfo;
	@RawRes
	private final int rawResId;

	public RawResourcePlayerSource(SourceInfo sourceInfo, int rawResId) {
		this.sourceInfo = sourceInfo;
		this.rawResId = rawResId;
	}

	@Override
	public SourceInfo getSourceInfo() {
		return this.sourceInfo;
	}

	@Override
	public void initializePlayerWithSource(Context context, MediaPlayer player) throws PlayerInitializeException {
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			AssetFileDescriptor afd = context.getResources().openRawResourceFd(this.rawResId);
			if (afd == null) {
				throw new PlayerInitializeException();
			}

			try {
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			} catch (IOException | IllegalArgumentException | SecurityException | IllegalStateException e) {
				throw new PlayerInitializeException();
			} finally {
				try {
					afd.close();
				} catch (IOException e) {
					Log.e(getClass().getSimpleName(), e.getMessage());
				}
			}
		} catch (Resources.NotFoundException e) {
			throw new PlayerInitializeException();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof RawResourcePlayerSource) {
			RawResourcePlayerSource source = (RawResourcePlayerSource) o;
			return this.sourceInfo.equals(source.sourceInfo)
					&& this.rawResId == source.rawResId;
		} else {
			return false;
		}
	}
}
