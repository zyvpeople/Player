package com.develop.zuzik.audioplayerexample.player.services;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

/**
 * User: zuzik
 * Date: 7/6/16
 */
public class PlaybackServiceInitializeBundle {
	public final PlayerSource playerSource;
	public final PlaybackFactory playbackFactory;
	public final PlaybackSettings playbackSettings;
	public final int notificationId;

	public PlaybackServiceInitializeBundle(PlayerSource playerSource,
										   PlaybackFactory playbackFactory,
										   PlaybackSettings playbackSettings,
										   int notificationId) {
		this.playerSource = playerSource;
		this.playbackFactory = playbackFactory;
		this.playbackSettings = playbackSettings;
		this.notificationId = notificationId;
	}
}
