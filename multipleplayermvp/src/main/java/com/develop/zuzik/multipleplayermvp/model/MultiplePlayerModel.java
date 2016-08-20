package com.develop.zuzik.multipleplayermvp.model;

import android.content.Context;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayermvp.composite.CompositeListener;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlaybackSettings;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */

public class MultiplePlayerModel<SourceInfo> implements MultiplePlayer.Model<SourceInfo> {

	private final MultiplePlayback<SourceInfo> playback;
	private final CompositeListener<SourceInfo> compositeListener = new CompositeListener<>();

	public MultiplePlayerModel(Context context,
							   MultiplePlaybackFactory<SourceInfo> multiplePlaybackFactory,
							   final MultiplePlaybackSettings playbackSettings) {
		this.playback = multiplePlaybackFactory.create(context);
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
			public void onUpdate(MultiplePlaybackState<SourceInfo> multiplePlaybackState) {
				compositeListener.onUpdate(multiplePlaybackState);
			}

			@Override
			public void onError(Throwable throwable) {
				compositeListener.onError(throwable);
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
	public Optional<MultiplePlaybackState<SourceInfo>> getState() {
		return Optional.of(this.playback.getMultiplePlaybackState());
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		this.playback.videoViewSetter(success);
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
