package com.develop.zuzik.audioplayerexample.player.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public interface PlayerNotificationFactory<SourceInfo> extends Serializable {
	Notification create(Context context,
						PlaybackState<SourceInfo> playbackState,
						PendingIntent playIntent,
						PendingIntent pauseIntent,
						PendingIntent stopIntent);
}
