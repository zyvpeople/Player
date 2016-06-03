package com.develop.zuzik.audioplayerexample.player.player_initializer;

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
public class RawResourcePlayerInitializer implements PlayerInitializer {
	@RawRes
	public final int rawResId;

	public RawResourcePlayerInitializer(int rawResId) {
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
}
