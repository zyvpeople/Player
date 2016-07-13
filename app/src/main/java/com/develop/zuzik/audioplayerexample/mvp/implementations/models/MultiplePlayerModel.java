package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.local.LocalMultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */

public class MultiplePlayerModel<SourceInfo> implements MultiplePlayer.Model<SourceInfo> {

	private final LocalMultiplePlayback<SourceInfo> playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();

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
		this.playback.setMultiplePlaybackListener(new MultiplePlaybackListener<SourceInfo>() {
			@Override
			public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
				playbackStateChangedPublishSubject.onNext(null);
				if (multiplePlaybackState.repeatSingle) {
					playbackSettings.repeatSingle();
				} else {
					playbackSettings.doNotRepeatSingle();
				}
				if (multiplePlaybackState.shuffle) {
					playbackSettings.shuffle();
				} else {
					playbackSettings.doNotShuffle();
				}
			}

			@Override
			public void onError(Throwable throwable) {
				errorPlayingPublishSubject.onNext(throwable);
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
	public Observable<Void> stateChangedObservable() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public Observable<Throwable> errorPlayingObservable() {
		return this.errorPlayingPublishSubject.asObservable();
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
