package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PublishSubject<PlaybackState<SourceInfo>> updatePublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPublishSubject = PublishSubject.create();
	private Optional<Playback<SourceInfo>> playback = Optional.absent();

	public PlayerModel(Context context, PlaybackSettings playbackSettings, PlaybackFactory<SourceInfo> playbackFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
	}

	@Override
	public void initWithSource(PlayerSource<SourceInfo> source) {
		if (this.playback.isPresent()) {
			if (!this.playback.get().getPlaybackState().playerSource.equals(source)) {
				releasePlayback(this.playback.get());
				initPlayback(source);
			}
		} else {
			initPlayback(source);
		}
	}

	@Override
	public void destroy() {
		getPlayback(value -> {
			releasePlayback(value);
			this.playback = Optional.absent();
		});
	}

	@Override
	public Optional<PlaybackState<SourceInfo>> getState() {
		return this.playback.transform(Playback::getPlaybackState);
	}

	@Override
	public Observable<PlaybackState<SourceInfo>> updateObservable() {
		return this.updatePublishSubject.asObservable();
	}

	@Override
	public Observable<Throwable> errorObservable() {
		return this.errorPublishSubject.asObservable();
	}

	@Override
	public void play() {
		getPlayback(Playback::play);
	}

	@Override
	public void pause() {
		getPlayback(Playback::pause);
	}

	@Override
	public void stop() {
		getPlayback(Playback::stop);
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		getPlayback(value -> value.seekTo(positionInMilliseconds));
	}

	@Override
	public void repeat() {
		getPlayback(Playback::repeat);
	}

	@Override
	public void doNotRepeat() {
		getPlayback(Playback::doNotRepeat);
	}

	@Override
	public void simulateError() {
		getPlayback(Playback::simulateError);
	}

	private void getPlayback(ParamAction<Playback<SourceInfo>> success) {
		if (this.playback.isPresent()) {
			success.execute(this.playback.get());
		}
	}

	private void initPlayback(PlayerSource<SourceInfo> source) {
		this.playback = Optional.of(this.playbackFactory.create(this.context, this.playbackSettings, source));
		this.playback.get().init();
		this.playback.get().setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate(PlaybackState playbackState) {
				updatePublishSubject.onNext(playbackState);
			}

			@Override
			public void onError(Throwable throwable) {
				errorPublishSubject.onNext(throwable);
			}
		});
	}

	private void releasePlayback(Playback<SourceInfo> playback) {
		playback.setPlaybackListener(null);
		playback.release();
	}
}
