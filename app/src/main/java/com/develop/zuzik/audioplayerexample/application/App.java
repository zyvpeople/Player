package com.develop.zuzik.audioplayerexample.application;

import android.app.Application;

import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;

/**
 * User: zuzik
 * Date: 6/22/16
 */
public class App extends Application {

	private PlayerModel<Song> model;

	public PlayerModel<Song> getModel() {
		return this.model;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.model = new PlayerModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>());
	}
}
