package com.develop.zuzik.audioplayerexample.player.player_initializer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.RawRes;

import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;

import java.io.IOException;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class UriPlayerInitializer implements PlayerInitializer {
	@RawRes
	public final Uri uri;

	public UriPlayerInitializer(Uri uri) {
		this.uri = uri;
	}

	@Override
	public void initialize(Context context, MediaPlayer player) throws PlayerInitializeException {
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
		} else if (o instanceof UriPlayerInitializer) {
			return this.uri.equals(((UriPlayerInitializer) o).uri);
		} else {
			return false;
		}
	}
}
