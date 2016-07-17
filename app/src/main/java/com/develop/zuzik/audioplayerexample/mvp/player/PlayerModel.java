package com.develop.zuzik.audioplayerexample.mvp.player;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.mvp.interfaces.Player;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackSettings;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final List<Listener<SourceInfo>> listeners = new ArrayList<>();
	private Optional<Playback<SourceInfo>> playback = Optional.absent();

	public PlayerModel(Context context, PlaybackSettings playbackSettings, PlaybackFactory<SourceInfo> playbackFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
		this.listeners.add(this.updateSettingsListener);
	}

	@Override
	public void setSource(PlayerSource<SourceInfo> source) {
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
	public void clear() {
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
		this.playback = Optional.of(this.playbackFactory.create(this.context, this.playbackSettings.isRepeat(), source));
		this.playback.get().init();
		this.playback.get().setPlaybackListener(new PlaybackListener<SourceInfo>() {
			@Override
			public void onUpdate(PlaybackState<SourceInfo> playbackState) {
				for (Listener<SourceInfo> listener : listeners) {
					listener.onUpdate(playbackState);
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

	private void releasePlayback(Playback<SourceInfo> playback) {
		playback.setPlaybackListener(null);
		playback.release();
	}

	private final Listener<SourceInfo> updateSettingsListener = new Listener<SourceInfo>() {
		@Override
		public void onUpdate(PlaybackState<SourceInfo> state) {
			if (state.repeat) {
				playbackSettings.repeat();
			} else {
				playbackSettings.doNotRepeat();
			}
		}

		@Override
		public void onError(Throwable error) {

		}
	};
}
