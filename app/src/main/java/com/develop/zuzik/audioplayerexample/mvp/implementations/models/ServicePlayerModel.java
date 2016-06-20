package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerModelState;
import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackService;
import com.fernandocejas.arrow.optional.Optional;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class ServicePlayerModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;
	private final PlayerSource<SourceInfo> source;
	private final Context context;

	public ServicePlayerModel(Context context, PlayerSource<SourceInfo> source) throws AudioServiceNotSupportException {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.source = source;
	}

	@Override
	public void init() {
		this.context.startService(PlaybackService.createForInit(this.context, source));
	}

	@Override
	public void destroy() {
//		this.context.stopService(PlaybackService.createForDestroy(this.context));
	}

	@Override
	public PlayerModelState<SourceInfo> getState() {
		return new PlayerModelState<>(new PlaybackState<>(State.IDLE, 0, Optional.absent(), this.repeat, this.source), this.repeat);
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
		this.context.startService(PlaybackService.createForPlay(this.context));
	}

	@Override
	public void pause() {
		this.context.startService(PlaybackService.createForPause(this.context));
	}

	@Override
	public void stop() {
		this.context.startService(PlaybackService.createForStop(this.context));
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
//		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat() {
		this.repeat = true;
//		this.playback.repeat();
	}

	@Override
	public void doNotRepeat() {
		this.repeat = false;
//		this.playback.doNotRepeat();
	}

	@Override
	public void simulateError() {
//		this.playback.simulateError();
	}
}
