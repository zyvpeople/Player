package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerModel<SourceInfo> implements MultiplePlayer.Model<SourceInfo> {

	private final MultiplePlayback<SourceInfo> playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;
	private boolean shuffle;

	public MultiplePlayerModel(Context context,
							   List<PlayerSource<SourceInfo>> initializers,
							   PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory,
							   PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory) {
		this.playback = new MultiplePlayback<>(context, initializers, nextPlayerSourceStrategyFactory, previousPlayerSourceStrategyFactory);
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setListener(new MultiplePlaybackListener() {
			@Override
			public void onUpdate() {
				playbackStateChangedPublishSubject.onNext(null);
			}

			@Override
			public void onError(Throwable throwable) {
				errorPlayingPublishSubject.onNext(throwable);
			}
		});
	}

	@Override
	public void destroy() {
		this.playback.setListener(null);
		this.playback.release();
	}

	@Override
	public MultiplePlayerModelState<SourceInfo> getState() {
		return new MultiplePlayerModelState<SourceInfo>(this.playback.getMultiplePlaybackState(), this.repeat, this.shuffle);
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
		this.playback.skipNext();
	}

	@Override
	public void skipPrevious() {
		this.playback.skipPrevious();
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
		this.playback.switchToPlayerSource(source);
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
