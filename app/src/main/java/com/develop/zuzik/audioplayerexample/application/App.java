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
import com.develop.zuzik.multipleplayermvp.model.MultiplePlayerModel;
import com.develop.zuzik.multipleplayermvp.model.MultiplePlayerServiceModel;
import com.develop.zuzik.multipleplayermvp.settings.InMemoryMultiplePlaybackSettings;
import com.develop.zuzik.player.broadcast_receiver.PlaybackBroadcastReceiver;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.local.LocalPlaybackFactory;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.develop.zuzik.playermvp.model.PlayerModel;
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
//TODO: add and remove player source -> get sources, add/remove any, set sources.
//TODO: add strategy for setSources method -> clear current playback or if current exist leave it play
//TODO: add strategy for switching between songs -> when current is pause should I do not play next/previous song or if current is playing should I play next/previous song
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
		this.model = new PlayerModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<Song>());
//		this.model = new PlayerServiceModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<Song>(), 100500, new SongPlayerNotificationFactory());

		InMemoryMultiplePlaybackSettings playbackSettings = new InMemoryMultiplePlaybackSettings();
		this.multiplePlayerModel = new MultiplePlayerModel<Song>(
				this,
				new LocalMultiplePlaybackFactory<Song>(
						new LocalPlaybackFactory<Song>(),
						new EndedNextPlayerSourceStrategy<Song>(),
						new EndedPreviousPlayerSourceStrategy<Song>(),
						new ExampleOnCompletePlayerSourceStrategyFactory<Song>(),
						playbackSettings.isRepeatSingle(),
						playbackSettings.isShuffle()),
				playbackSettings);
//		this.multiplePlayerModel = new MultiplePlayerServiceModel<Song>(
//				this,
//				playbackSettings,
//				new LocalMultiplePlaybackFactory<>(
//						new LocalPlaybackFactory<Song>(),
//						new EndedNextPlayerSourceStrategy<Song>(),
//						new EndedPreviousPlayerSourceStrategy<Song>(),
//						new ExampleOnCompletePlayerSourceStrategyFactory<Song>(),
//						playbackSettings.isRepeatSingle(),
//						playbackSettings.isShuffle()),
//				100500,
//				new SongMultiplePlayerNotificationFactory());

		PlaybackBroadcastReceiver.register(this, new Action() {
			@Override
			public void execute() {
				model.pause();
			}
		});
		PlaybackBroadcastReceiver.register(this, new Action() {
			@Override
			public void execute() {
				multiplePlayerModel.pause();
			}
		});
	}
}
