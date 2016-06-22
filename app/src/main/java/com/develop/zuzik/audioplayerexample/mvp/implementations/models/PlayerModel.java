package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.settings.PlaybackSettings;
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

	private Optional<Playback<SourceInfo>> playback = Optional.absent();
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private final Context context;
	private final PlaybackSettings playbackSettings;

	public PlayerModel(Context context, PlaybackSettings playbackSettings) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
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

	private void initPlayback(PlayerSource<SourceInfo> source) {
		this.playback = Optional.of(new Playback<SourceInfo>(this.context, source, this.playbackSettings));
		this.playback.get().init();
		this.playback.get().setPlaybackListener(new PlaybackListener() {
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
		getPlayback(value -> {
			releasePlayback(value);
			this.playback = Optional.absent();
		});
	}

	private void releasePlayback(Playback<SourceInfo> playback) {
		playback.setPlaybackListener(null);
		playback.release();
	}

	@Override
	public Optional<PlaybackState<SourceInfo>> getState() {
		return this.playback.transform(Playback::getPlaybackState);
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
}
