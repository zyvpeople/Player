package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategy;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.local.LocalMultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.settings.InMemoryMultiplePlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlaybackFactory;
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
	private boolean repeat;
	private boolean shuffle;

	private List<PlayerSource<SourceInfo>> initializers;

	public MultiplePlayerModel(Context context,
							   List<PlayerSource<SourceInfo>> initializers,
							   PlayerSourceStrategy<SourceInfo> nextPlayerSourceStrategy,
							   PlayerSourceStrategy<SourceInfo> previousPlayerSourceStrategy,
							   PlayerSourceStrategyFactory<SourceInfo> onCompletePlayerSourceStrategyFactory) {
		this.playback = new LocalMultiplePlayback<>(
				context,
				new LocalPlaybackFactory<>(),
				new InMemoryMultiplePlaybackSettings(),
				nextPlayerSourceStrategy,
				previousPlayerSourceStrategy,
				onCompletePlayerSourceStrategyFactory);
		this.initializers = initializers;
	}

	@Override
	public void init() {
		this.playback.setMultiplePlaybackListener(new MultiplePlaybackListener<SourceInfo>() {
			@Override
			public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
				playbackStateChangedPublishSubject.onNext(null);
			}

			@Override
			public void onError(Throwable throwable) {
				errorPlayingPublishSubject.onNext(throwable);
			}
		});
		this.playback.setPlayerSources(this.initializers);
	}

	@Override
	public void destroy() {
		this.playback.setMultiplePlaybackListener(null);
		this.playback.clear();
	}

	@Override
	public MultiplePlayerModelState<SourceInfo> getState() {
		return new MultiplePlayerModelState<>(this.playback.getMultiplePlaybackState(), this.repeat, this.shuffle);
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
		this.repeat = true;
		this.playback.repeatSingle();
	}

	@Override
	public void doNotRepeatSingle() {
		this.repeat = false;
		this.playback.doNotRepeatSingle();
	}

	@Override
	public void shuffle() {
		this.shuffle = true;
		this.playback.shuffle();
	}

	@Override
	public void doNotShuffle() {
		this.shuffle = false;
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
