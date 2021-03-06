package com.develop.zuzik.multipleplayer.interfaces;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public interface MultiplePlayerNotificationFactory<SourceInfo> extends Serializable {
	Notification create(Context context,
						MultiplePlaybackState<SourceInfo> playbackState,
						PendingIntent playIntent,
						PendingIntent pauseIntent,
						PendingIntent stopIntent,
						PendingIntent playNextIntent,
						PendingIntent playPreviousIntent);
}
