package com.develop.zuzik.audioplayerexample.application;

import android.app.Application;

import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerServiceModel;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.presentation.notifications.SongNotificationFactory;

/**
 * User: zuzik
 * Date: 6/22/16
 */
//TODO: implement doze mode
//TODO: implement sound volume when sms
//TODO: implement sound volume when headphone is detached from device
//TODO: create widget for launcher
//TODO: rework PlaybackSettings -> playback must not depend on this class, settings must be only in model
//TODO: for playbacks add multiple listeners instead of one
//TODO: add static methods for null objects
//TODO: try to create playback without init method and ability to set player source not in constructor
public class App extends Application {

	private Player.Model<Song> model;

	public Player.Model<Song> getModel() {
		return this.model;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		this.model = new PlayerModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>());
		this.model = new PlayerServiceModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>(), 100500, new SongNotificationFactory());
	}
}
