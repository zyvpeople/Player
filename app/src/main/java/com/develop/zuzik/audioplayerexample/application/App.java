package com.develop.zuzik.audioplayerexample.application;

import android.app.Application;

import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.example.factories.ExampleOnCompletePlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.mvp.MultiplePlayer;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.multiple_player.MultiplePlayerServiceModel;
import com.develop.zuzik.audioplayerexample.mvp.player.PlayerServiceModel;
import com.develop.zuzik.multipleplayer.local.LocalMultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedNextPlayerSourceStrategy;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedPreviousPlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.InMemoryMultiplePlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.playback.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.presentation.notifications.SongMultiplePlayerNotificationFactory;
import com.develop.zuzik.audioplayerexample.presentation.notifications.SongPlayerNotificationFactory;
import com.develop.zuzik.player.local.LocalPlaybackFactory;

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
//TODO: try to create playback without init method and ability to set player source not in constructor
//TODO: use index of current playback instead playerSource because there is an possibility to set same object to queue few times
//TODO: create strategy of bool parameter - when setPlayerSources - play right now or do not play
//TODO: replace settings to mvp package
//TODO: refactor packages
//TODO: create android library for player
//FIXME: playback service receive playback factory and check if playback exist -> but when we set another factory for differ playback class so this logic is incorrect
//TODO: service - communicate with binder
//TODO: UriPlayerSource - Uri is not serializable
//TODO: use jack for lambda
public class App extends Application {

	private Player.Model<Song> model;
	private MultiplePlayer.Model<Song> multiplePlayerModel;

	public Player.Model<Song> getModel() {
		return this.model;
	}

	public MultiplePlayer.Model<Song> getMultiplePlayerModel() {
		return this.multiplePlayerModel;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		this.model = new PlayerModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>());
		this.model = new PlayerServiceModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>(), 100500, new SongPlayerNotificationFactory());

		InMemoryMultiplePlaybackSettings playbackSettings = new InMemoryMultiplePlaybackSettings();
//		this.multiplePlayerModel = new MultiplePlayerModel<>(
//				this,
//				new LocalMultiplePlaybackFactory<>(
//						new LocalPlaybackFactory<>(),
//						new EndedNextPlayerSourceStrategy<>(),
//						new EndedPreviousPlayerSourceStrategy<>(),
//						new ExampleOnCompletePlayerSourceStrategyFactory<>(),
//						playbackSettings.isRepeatSingle(),
//						playbackSettings.isShuffle()),
//				playbackSettings);
		this.multiplePlayerModel = new MultiplePlayerServiceModel<>(
				this,
				playbackSettings,
				new LocalMultiplePlaybackFactory<>(
						new LocalPlaybackFactory<>(),
						new EndedNextPlayerSourceStrategy<>(),
						new EndedPreviousPlayerSourceStrategy<>(),
						new ExampleOnCompletePlayerSourceStrategyFactory<>(),
						playbackSettings.isRepeatSingle(),
						playbackSettings.isShuffle()),
				100500,
				new SongMultiplePlayerNotificationFactory());
	}
}
