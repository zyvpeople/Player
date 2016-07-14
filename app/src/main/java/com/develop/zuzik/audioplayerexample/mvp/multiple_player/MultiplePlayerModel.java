package com.develop.zuzik.audioplayerexample.mvp.multiple_player;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.local.LocalMultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */

public class MultiplePlayerModel<SourceInfo> implements MultiplePlayer.Model<SourceInfo> {

	private final LocalMultiplePlayback<SourceInfo> playback;
	private final List<Listener<SourceInfo>> listeners = new ArrayList<>();

	public MultiplePlayerModel(Context context,
							   PlaybackFactory<SourceInfo> playbackFactory,
							   PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy,
							   PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy,
							   PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory,
							   MultiplePlaybackSettings playbackSettings) {
		this.playback = new LocalMultiplePlayback<>(
				context,
				playbackFactory,
				nextPlayerSourceStrategy,
				previousPlayerSourceStrategy,
				onCompletePlayerSourceStrategyFactory,
				playbackSettings.isRepeatSingle(),
				playbackSettings.isShuffle());
		addListener(new Listener<SourceInfo>() {
			@Override
			public void onUpdate(MultiplePlaybackState<SourceInfo> state) {
				if (state.repeatSingle) {
					playbackSettings.repeatSingle();
				} else {
					playbackSettings.doNotRepeatSingle();
				}
				if (state.shuffle) {
					playbackSettings.shuffle();
				} else {
					playbackSettings.doNotShuffle();
				}
			}

			@Override
			public void onError(Throwable error) {
			}
		});
		this.playback.setMultiplePlaybackListener(new MultiplePlaybackListener<SourceInfo>() {
			@Override
			public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
				for (Listener<SourceInfo> listener : listeners) {
					listener.onUpdate(multiplePlaybackState);
				}
			}

			@Override
			public void onError(Throwable throwable) {
				for (Listener<SourceInfo> listener : listeners) {
					listener.onError(throwable);
				}
			}
		});
	}

	@Override
	public void setSources(List<PlayerSource<SourceInfo>> playerSources) {
		this.playback.setPlayerSources(playerSources);
	}

	@Override
	public void clear() {
		this.playback.setMultiplePlaybackListener(null);
		this.playback.clear();
	}

	@Override
	public MultiplePlaybackState<SourceInfo> getState() {
		return this.playback.getMultiplePlaybackState();
	}

	@Override
	public void addListener(Listener<SourceInfo> listener) {
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void removeListener(Listener<SourceInfo> listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void play() {
		this.playback.play();
	}

	@Override
	public void pause() {
		this.playback.pause();
	}

	@Override
	public void stop() {
		this.playback.stop();
	}

	@Override
	public void skipNext() {
		this.playback.playNextPlayerSource();
	}

	@Override
	public void skipPrevious() {
		this.playback.playPreviousPlayerSource();
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeatSingle() {
		this.playback.repeatSingle();
	}

	@Override
	public void doNotRepeatSingle() {
		this.playback.doNotRepeatSingle();
	}

	@Override
	public void shuffle() {
		this.playback.shuffle();
	}

	@Override
	public void doNotShuffle() {
		this.playback.doNotShuffle();
	}

	@Override
	public void switchToSource(PlayerSource<SourceInfo> source) {
		this.playback.playPlayerSource(source);
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
