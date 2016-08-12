package com.develop.zuzik.player.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.develop.zuzik.player.interfaces.Playback;

/**
 * User: zuzik
 * Date: 8/12/16
 */
public class PlaybackBroadcastReceiver extends BroadcastReceiver {

	public static PlaybackBroadcastReceiver register(Context context, Playback playback) {
		PlaybackBroadcastReceiver receiver = new PlaybackBroadcastReceiver(playback);
		context.registerReceiver(receiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
		return receiver;
	}

	private final Playback playback;

	private PlaybackBroadcastReceiver(Playback playback) {
		this.playback = playback;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
			this.playback.pause();
		}
	}
}
