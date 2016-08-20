package com.develop.zuzik.player.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.Playback;

/**
 * User: zuzik
 * Date: 8/12/16
 */
public class PlaybackBroadcastReceiver extends BroadcastReceiver {

	public static PlaybackBroadcastReceiver register(Context context, Action pause) {
		PlaybackBroadcastReceiver receiver = new PlaybackBroadcastReceiver(pause);
		context.registerReceiver(receiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
		return receiver;
	}

	private final Action pause;

	private PlaybackBroadcastReceiver(Action pause) {
		this.pause = pause;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
			this.pause.execute();
		}
	}
}
