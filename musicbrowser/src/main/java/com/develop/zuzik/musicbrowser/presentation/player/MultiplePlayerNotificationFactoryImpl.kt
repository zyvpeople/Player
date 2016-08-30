package com.develop.zuzik.musicbrowser.presentation.player

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.domain.entity.Song

/**
 * User: zuzik
 * Date: 8/24/16
 */
class MultiplePlayerNotificationFactoryImpl : MultiplePlayerNotificationFactory<Song> {
    override fun create(
            context: Context,
            playbackState: MultiplePlaybackState<Song>,
            playIntent: PendingIntent,
            pauseIntent: PendingIntent,
            stopIntent: PendingIntent,
            playNextIntent: PendingIntent,
            playPreviousIntent: PendingIntent,
            destroyServiceIntent: PendingIntent): Notification {
        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        return NotificationCompat
                .Builder(context)
                .setStyle(android.support.v7.app.NotificationCompat.MediaStyle())
                .setContentTitle(playbackState.currentPlaybackState
                        .transform({ input -> input.playerSource.sourceInfo.author }).or("Player")).setContentText(playbackState.currentPlaybackState.transform { input -> input.playerSource.sourceInfo.name }.or("Player")).setTicker("Ticker").setSmallIcon(R.drawable.ic_stat_name)
                .setProgress(
                        playbackState.currentPlaybackState.transform { input -> input.maxTimeInMilliseconds.or(100) }.or(100),
                        playbackState.currentPlaybackState.transform { input -> input.currentTimeInMilliseconds }.or(0),
                        false)
                .setOngoing(true).setAutoCancel(true).addAction(R.drawable.ic_stat_name, "Play", playIntent).addAction(R.drawable.ic_stat_name, "Pause", pauseIntent).addAction(R.drawable.ic_stat_name, "Stop", stopIntent).build()

    }
}