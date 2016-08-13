package com.develop.zuzik.audioplayerexample.application;

import android.app.Application;

import com.develop.zuzik.audioplayerexample.domain.ExampleOnCompletePlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.notifications.SongMultiplePlayerNotificationFactory;
import com.develop.zuzik.audioplayerexample.presentation.notifications.SongPlayerNotificationFactory;
import com.develop.zuzik.multipleplayer.local.LocalMultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedNextPlayerSourceStrategy;
import com.develop.zuzik.multipleplayer.player_source_strategy.EndedPreviousPlayerSourceStrategy;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.model.MultiplePlayerServiceModel;
import com.develop.zuzik.multipleplayermvp.settings.InMemoryMultiplePlaybackSettings;
import com.develop.zuzik.player.local.LocalPlaybackFactory;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.playermvp.model.PlayerServiceModel;
import com.develop.zuzik.playermvp.settings.InMemoryPlaybackSettings;

/**
 * User: zuzik
 * Date: 6/22/16
 */
//TODO: create widget for launcher
//TODO: use index of current playback instead playerSource because there is an possibility to set same object to queue few times
//FIXME: playback service receive playback factory and check if playback exist -> but when we set another factory for differ playback class so this logic is incorrect
//TODO: use jack for lambda
//TODO: in notification display X to close notification
//TODO: do not use optional from library
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
