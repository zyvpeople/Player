package com.develop.zuzik.audioplayerexample.presentation.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

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
			PendingIntent playNextIntent,
			PendingIntent playPreviousIntent,
			PendingIntent destroyServiceIntent) {
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		return new NotificationCompat.Builder(context)
				.setStyle(new android.support.v7.app.NotificationCompat.MediaStyle())
				.setContentTitle(playbackState.currentPlaybackState.transform(new Function<PlaybackState<Song>, String>() {
					@Nullable
					@Override
					public String apply(PlaybackState<Song> input) {
						return input.playerSource.getSourceInfo().artist;
					}
				}).or("Player"))
				.setContentText(playbackState.currentPlaybackState.transform(new Function<PlaybackState<Song>, String>() {
					@Nullable
					@Override
					public String apply(PlaybackState<Song> input) {
						return input.playerSource.getSourceInfo().name;
					}
				}).or("Player"))
				.setTicker("Ticker")
				.setSmallIcon(R.drawable.ic_stat_name)
//				.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
				.setProgress(
						playbackState.currentPlaybackState.transform(new Function<PlaybackState<Song>, Integer>() {
							@Nullable
							@Override
							public Integer apply(PlaybackState<Song> input) {
								return input.maxTimeInMilliseconds.or(100);
							}
						}).or(100),
						playbackState.currentPlaybackState.transform(new Function<PlaybackState<Song>, Integer>() {
							@Nullable
							@Override
							public Integer apply(PlaybackState<Song> input) {
								return input.currentTimeInMilliseconds;
							}
						}).or(0),
						false)
				.setOngoing(true)
				.setAutoCancel(true)
				.addAction(R.drawable.ic_stat_name, "Play", playIntent)
				.addAction(R.drawable.ic_stat_name, "Pause", pauseIntent)
				.addAction(R.drawable.ic_stat_name, "Stop", destroyServiceIntent)
				.build();
	}
}