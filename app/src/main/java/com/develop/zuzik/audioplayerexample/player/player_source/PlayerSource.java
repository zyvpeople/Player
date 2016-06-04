package com.develop.zuzik.audioplayerexample.player.player_source;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerSource {
	void initialize(Context context, MediaPlayer player) throws PlayerInitializeException;
}
