package com.develop.zuzik.audioplayerexample.presentation.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.player.notification.NotificationFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class SongNotificationFactory implements NotificationFactory<Song> {
	@Override
	public Notification create(Context context,
							   PlaybackState<Song> playbackState,
							   PendingIntent playIntent,
							   PendingIntent pauseIntent,
							   PendingIntent stopIntent) {
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		return new NotificationCompat.Builder(context)
				.setContentTitle(playbackState.playerSource.getSourceInfo().artist)
				.setContentText(playbackState.playerSource.getSourceInfo().name)
				.setTicker("Ticker")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
				.setProgress(playbackState.maxTimeInMilliseconds.or(100), playbackState.currentTimeInMilliseconds, false)
				.setOngoing(true)
				.addAction(R.drawable.ic_play, "Play", playIntent)
				.addAction(R.drawable.ic_pause, "Pause", pauseIntent)
				.addAction(R.drawable.ic_stop, "Stop", stopIntent)
				.build();
	}
}