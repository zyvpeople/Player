package com.develop.zuzik.audioplayerexample.presentation.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.notification.MultiplePlayerNotificationFactory;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class SongMultiplePlayerNotificationFactory implements MultiplePlayerNotificationFactory<Song> {

	@Override
	public Notification create(
			Context context,
			MultiplePlaybackState<Song> playbackState,
			PendingIntent playIntent,
			PendingIntent pauseIntent,
			PendingIntent stopIntent,
			PendingIntent playNext,
			PendingIntent playPrevious) {
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		return new NotificationCompat.Builder(context)
				.setStyle(new android.support.v7.app.NotificationCompat.MediaStyle())
				.setContentTitle(playbackState.currentPlaybackState.transform(input -> input.playerSource.getSourceInfo().artist).or(""))
				.setContentText(playbackState.currentPlaybackState.transform(input -> input.playerSource.getSourceInfo().name).or(""))
				.setTicker("Ticker")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
				.setProgress(
						playbackState.currentPlaybackState.transform(input -> input.maxTimeInMilliseconds.or(100)).or(100),
						playbackState.currentPlaybackState.transform(input -> input.currentTimeInMilliseconds).or(0),
						false)
				.setOngoing(true)
				.setAutoCancel(true)
				.addAction(R.drawable.ic_stop, "<", playPrevious)
				.addAction(R.drawable.ic_play, "Play", playIntent)
				.addAction(R.drawable.ic_pause, "Pause", pauseIntent)
				.addAction(R.drawable.ic_stop, "Stop", stopIntent)
				.addAction(R.drawable.ic_stop, ">", playNext)
				.build();
	}
}