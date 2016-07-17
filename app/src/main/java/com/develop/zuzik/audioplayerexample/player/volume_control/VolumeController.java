package com.develop.zuzik.audioplayerexample.player.volume_control;

import android.app.Activity;
import android.media.AudioManager;

/**
 * User: zuzik
 * Date: 6/11/16
 */
//TODO: implement and use
public class VolumeController {
	public void useVolumeKeysToControlPlaybackVolume(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}
