package com.develop.zuzik.audioplayerexample.player.player_source;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;

import java.io.IOException;

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

	@Override
	public void initialize(Context context, MediaPlayer player) throws PlayerInitializeException {
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
			return this.rawResId == ((RawResourcePlayerSource) o).rawResId;
		} else {
			return false;
		}
	}
}
