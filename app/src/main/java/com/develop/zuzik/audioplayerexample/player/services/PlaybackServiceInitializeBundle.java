package com.develop.zuzik.audioplayerexample.player.services;

import com.develop.zuzik.audioplayerexample.player.notification.NotificationFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class PlaybackServiceInitializeBundle<SourceInfo> implements Serializable {
	public final PlayerSource<SourceInfo> playerSource;
	public final PlaybackFactory<SourceInfo> playbackFactory;
	public final PlaybackSettings playbackSettings;
	public final int notificationId;
	public final NotificationFactory<SourceInfo> notificationFactory;

	public PlaybackServiceInitializeBundle(PlayerSource<SourceInfo> playerSource,
										   PlaybackFactory<SourceInfo> playbackFactory,
										   PlaybackSettings playbackSettings,
										   int notificationId,
										   NotificationFactory<SourceInfo> notificationFactory) {
		this.playerSource = playerSource;
		this.playbackFactory = playbackFactory;
		this.playbackSettings = playbackSettings;
		this.notificationId = notificationId;
		this.notificationFactory = notificationFactory;
	}
}
