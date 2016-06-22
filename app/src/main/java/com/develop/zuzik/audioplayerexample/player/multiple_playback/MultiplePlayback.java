package com.develop.zuzik.audioplayerexample.player.multiple_playback;

import android.content.Context;
import android.content.ContextWrapper;

import com.develop.zuzik.audioplayerexample.player.multiple_playback.strategies.factories.PlayerSourceStrategyFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlayback;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//TODO: add logic for add and remove songs from list
//TODO: add logic for start any song and not only next and previous
public class MultiplePlayback<SourceInfo> {

	private MultiplePlaybackState<SourceInfo> multiplePlaybackState;
	private Optional<LocalPlayback<SourceInfo>> currentPlayback;

	private MultiplePlaybackListener listener = new NullMultiplePlaybackListener();
	private final PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory;
	private final PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory;
	private final Context context;

	public MultiplePlayback(
			Context context,
			List<PlayerSource<SourceInfo>> playerSources,
			PlayerSourceStrategyFactory<SourceInfo> nextPlayerSourceStrategyFactory,
			PlayerSourceStrategyFactory<SourceInfo> previousPlayerSourceStrategyFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.nextPlayerSourceStrategyFactory = nextPlayerSourceStrategyFactory;
		this.previousPlayerSourceStrategyFactory = previousPlayerSourceStrategyFactory;
		this.currentPlayback = playerSources.isEmpty()
				? Optional.absent()
				: Optional.of(new LocalPlayback<SourceInfo>(this.context, new InMemoryPlaybackSettings(), playerSources.get(0)));
		this.multiplePlaybackState = new MultiplePlaybackState<SourceInfo>(
				playerSources,
				this.currentPlayback.transform(LocalPlayback::getPlaybackState),
				false,
				false);
	}

	public void setListener(MultiplePlaybackListener listener) {
		this.listener = listener != null
				? listener
				: new NullMultiplePlaybackListener();
	}

	public MultiplePlaybackState<SourceInfo> getMultiplePlaybackState() {
		return this.multiplePlaybackState;
	}

	private void currentPlayback(ParamAction<LocalPlayback<SourceInfo>> action) {
		if (this.currentPlayback.isPresent()) {
			action.execute(this.currentPlayback.get());
		}
	}

	public void repeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withRepeatSingle(true);
		currentPlayback(LocalPlayback::repeat);
	}

	public void doNotRepeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withRepeatSingle(false);
		currentPlayback(LocalPlayback::doNotRepeat);
	}

	public void shuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withShuffle(true);
		this.listener.onUpdate();
	}

	public void doNotShuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.withShuffle(false);
		this.listener.onUpdate();
	}

	//region Play

	public void init() {
		currentPlayback(result -> initPlayback(result, false));
	}

	public void release() {
		currentPlayback(this::releasePlayback);
	}

	public void play() {
		currentPlayback(LocalPlayback::play);
	}

	public void pause() {
		currentPlayback(LocalPlayback::pause);
	}

	public void stop() {
		currentPlayback(LocalPlayback::stop);
	}

	public void seekTo(int positionInMilliseconds) {
		currentPlayback(result -> result.seekTo(positionInMilliseconds));
	}

	public void skipNext() {
		nextPlayback(nextPlayback ->
				switchFromOldToNewPlayback(this.currentPlayback, nextPlayback));
	}

	public void skipPrevious() {
		previousPlayback(previousPlayback ->
				switchFromOldToNewPlayback(this.currentPlayback, previousPlayback));
	}

	public void switchToPlayerSource(PlayerSource<SourceInfo> playerSource) {
		if (this.currentPlayback.isPresent()
				&& this.currentPlayback.get().getPlaybackState().playerSource.equals(playerSource)) {
			return;
		}
		int indexOfPlayerSource = this.multiplePlaybackState.playerSources.indexOf(playerSource);
		Optional<LocalPlayback<SourceInfo>> newPlayback = indexOfPlayerSource != -1
				? Optional.of(new LocalPlayback<SourceInfo>(this.context, new InMemoryPlaybackSettings(), this.multiplePlaybackState.playerSources.get(indexOfPlayerSource)))
				: Optional.absent();
		switchFromOldToNewPlayback(this.currentPlayback, newPlayback);
	}

	private void switchFromOldToNewPlayback(Optional<LocalPlayback<SourceInfo>> oldPlayback, Optional<LocalPlayback<SourceInfo>> newPlayback) {
		if (oldPlayback.isPresent() && newPlayback.isPresent()) {
			releasePlayback(oldPlayback.get());
			this.currentPlayback = Optional.absent();
		}
		if (newPlayback.isPresent()) {
			this.currentPlayback = newPlayback;
			initPlayback(newPlayback.get(), true);
		}
		this.multiplePlaybackState = this.multiplePlaybackState.withCurrentPlaybackState(
				this.currentPlayback.transform(LocalPlayback::getPlaybackState));
	}

	private void initPlayback(LocalPlayback<SourceInfo> playback, boolean play) {
		playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate() {
				PlaybackState<SourceInfo> bundle = playback.getPlaybackState();
				multiplePlaybackState = multiplePlaybackState.withCurrentPlaybackState(Optional.of(bundle));
				listener.onUpdate();
				if (bundle.state == State.COMPLETED) {
					skipNext();
				}
			}

			@Override
			public void onError(Throwable throwable) {
				multiplePlaybackState = multiplePlaybackState.withCurrentPlaybackState(Optional.of(playback.getPlaybackState()));
				listener.onError(throwable);
				skipNext();
			}
		});
		playback.init();

		if (this.multiplePlaybackState.repeatSingle) {
			playback.repeat();
		} else {
			playback.doNotRepeat();
		}

		if (play) {
			playback.play();
		}
	}

	private void releasePlayback(LocalPlayback<SourceInfo> playback) {
		playback.release();
		playback.setPlaybackListener(null);
	}

	private void nextPlayback(ParamAction<Optional<LocalPlayback<SourceInfo>>> action) {
		currentPlayback(currentPlayback -> {
			Optional<PlayerSource<SourceInfo>> playerInitializer = this.nextPlayerSourceStrategyFactory
					.create(this.multiplePlaybackState.shuffle)
					.determine(this.multiplePlaybackState.playerSources, currentPlayback.getPlayerSource());
			if (playerInitializer.isPresent()) {
				action.execute(Optional.of(new LocalPlayback<>(this.context, new InMemoryPlaybackSettings(), playerInitializer.get())));
			} else {
				action.execute(Optional.absent());
			}
		});
	}

	private void previousPlayback(ParamAction<Optional<LocalPlayback<SourceInfo>>> action) {
		currentPlayback(currentPlayback -> {
			Optional<PlayerSource<SourceInfo>> playerInitializer = this.previousPlayerSourceStrategyFactory
					.create(this.multiplePlaybackState.shuffle)
					.determine(this.multiplePlaybackState.playerSources, currentPlayback.getPlayerSource());
			if (playerInitializer.isPresent()) {
				action.execute(Optional.of(new LocalPlayback<>(this.context, new InMemoryPlaybackSettings(), playerInitializer.get())));
			} else {
				action.execute(Optional.absent());
			}
		});
	}

	//endregion

	//region Fake

	public void simulateError() {
		currentPlayback(LocalPlayback::simulateError);
	}

	//endregion
}
