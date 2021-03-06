package com.develop.zuzik.playermvp.model;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.playermvp.composite.CompositeListener;
import com.develop.zuzik.playermvp.interfaces.PlaybackSettings;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final CompositeListener<SourceInfo> compositeListener = new CompositeListener<>();
	private Optional<Playback<SourceInfo>> playback = Optional.absent();

	public PlayerModel(Context context, final PlaybackSettings playbackSettings, PlaybackFactory<SourceInfo> playbackFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
		Listener<SourceInfo> updateSettingsListener = new Listener<SourceInfo>() {
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
		this.compositeListener.addListener(updateSettingsListener);
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
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> value) {
				PlayerModel.this.releasePlayback(value);
				PlayerModel.this.playback = Optional.absent();
			}
		});
	}

	@Override
	public Optional<PlaybackState<SourceInfo>> getState() {
		return this.playback.transform(new Function<Playback<SourceInfo>, PlaybackState<SourceInfo>>() {
			@Nullable
			@Override
			public PlaybackState<SourceInfo> apply(Playback<SourceInfo> sourceInfoPlayback) {
				return sourceInfoPlayback.getPlaybackState();
			}
		});
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.playback.isPresent()) {
			this.playback.get().videoViewSetter(success);
		}
	}

	@Override
	public void addListener(Listener<SourceInfo> listener) {
		this.compositeListener.addListener(listener);
	}

	@Override
	public void removeListener(Listener<SourceInfo> listener) {
		this.compositeListener.removeListener(listener);
	}

	@Override
	public void play() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.play();
			}
		});
	}

	@Override
	public void pause() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.pause();
			}
		});
	}

	@Override
	public void stop() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.stop();
			}
		});
	}

	@Override
	public void seekToPosition(final int positionInMilliseconds) {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> value) {
				value.seekTo(positionInMilliseconds);
			}
		});
	}

	@Override
	public void repeat() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.repeat();
			}
		});
	}

	@Override
	public void doNotRepeat() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.doNotRepeat();
			}
		});
	}

	@Override
	public void simulateError() {
		getPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.simulateError();
			}
		});
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
				compositeListener.onUpdate(playbackState);
			}

			@Override
			public void onError(Throwable throwable) {
				compositeListener.onError(throwable);
			}
		});
	}

	private void releasePlayback(Playback<SourceInfo> playback) {
		playback.setPlaybackListener(null);
		playback.release();
	}

}
