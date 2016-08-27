package com.develop.zuzik.multipleplayer.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceReleaseStrategy;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceDetermineStrategyFactory;
import com.develop.zuzik.multipleplayer.null_object.NullMultiplePlaybackListener;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.null_object.NullAction;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class LocalMultiplePlayback<SourceInfo> implements MultiplePlayback<SourceInfo> {

	private final Context context;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> nextPlayerSourceDetermineStrategyFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> previousPlayerSourceDetermineStrategyFactory;
	private final PlayerSourceDetermineStrategyFactory<SourceInfo> onCompletePlayerSourceDetermineStrategyFactory;
	private final PlayerSourceReleaseStrategy<SourceInfo> releaseStrategy;
	private MultiplePlaybackState<SourceInfo> multiplePlaybackState;
	private MultiplePlaybackListener<SourceInfo> multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
	private Optional<Playback<SourceInfo>> currentPlayback = Optional.absent();

	public LocalMultiplePlayback(Context context,
								 PlaybackFactory<SourceInfo> playbackFactory,
								 PlayerSourceDetermineStrategyFactory<SourceInfo> nextPlayerSourceDetermineStrategyFactory,
								 PlayerSourceDetermineStrategyFactory<SourceInfo> previousPlayerSourceDetermineStrategyFactory,
								 PlayerSourceDetermineStrategyFactory<SourceInfo> onCompletePlayerSourceDetermineStrategyFactory,
								 PlayerSourceReleaseStrategy<SourceInfo> releaseStrategy,
								 boolean repeatSingle,
								 boolean shuffle) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackFactory = playbackFactory;
		this.nextPlayerSourceDetermineStrategyFactory = nextPlayerSourceDetermineStrategyFactory;
		this.previousPlayerSourceDetermineStrategyFactory = previousPlayerSourceDetermineStrategyFactory;
		this.onCompletePlayerSourceDetermineStrategyFactory = onCompletePlayerSourceDetermineStrategyFactory;
		this.releaseStrategy = releaseStrategy;
		this.multiplePlaybackState = new MultiplePlaybackState<>(
				new ArrayList<PlayerSource<SourceInfo>>(),
				Optional.<PlaybackState<SourceInfo>>absent(),
				repeatSingle,
				shuffle);
	}

	@Override
	public MultiplePlaybackState<SourceInfo> getMultiplePlaybackState() {
		return this.multiplePlaybackState;
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.currentPlayback.isPresent()) {
			this.currentPlayback.get().videoViewSetter(success);
		}
	}

	@Override
	public void setMultiplePlaybackListener(MultiplePlaybackListener<SourceInfo> multiplePlaybackListener) {
		this.multiplePlaybackListener = multiplePlaybackListener != null
				? multiplePlaybackListener
				: NullMultiplePlaybackListener.<SourceInfo>getInstance();
	}

	@Override
	public void clear() {
		releaseCurrentPlaybackAndUpdateState();
		this.multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
		this.multiplePlaybackState = new MultiplePlaybackState<>(
				new ArrayList<PlayerSource<SourceInfo>>(),
				Optional.<PlaybackState<SourceInfo>>absent(),
				false,
				false);
	}

	@Override
	public void play() {
		currentPlayback(
				new ParamAction<Playback<SourceInfo>>() {
					@Override
					public void execute(Playback<SourceInfo> sourceInfoPlayback) {
						sourceInfoPlayback.play();
					}
				},
				new Action() {
					@Override
					public void execute() {
						boolean hasPlayerSources = !LocalMultiplePlayback.this.getMultiplePlaybackState().playerSources.isEmpty();
						if (hasPlayerSources) {
							PlayerSource<SourceInfo> playerSource = LocalMultiplePlayback.this.getMultiplePlaybackState().playerSources.get(0);
							LocalMultiplePlayback.this.setAndInitAndPlayCurrentPlayback(playerSource);
						}
					}
				});
	}

	@Override
	public void pause() {
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.pause();
			}
		});
	}

	@Override
	public void stop() {
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.stop();
			}
		});
	}

	@Override
	public void seekTo(final int positionInMilliseconds) {
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> playback) {
				playback.seekTo(positionInMilliseconds);
			}
		});
	}

	@Override
	public void repeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().repeatSingle(true).build();
		currentPlayback(
				new ParamAction<Playback<SourceInfo>>() {
					@Override
					public void execute(Playback<SourceInfo> sourceInfoPlayback) {
						sourceInfoPlayback.repeat();
					}
				},
				new Action() {
					@Override
					public void execute() {
						LocalMultiplePlayback.this.notifyStateChanged();
					}
				});
	}

	@Override
	public void doNotRepeatSingle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().repeatSingle(false).build();
		currentPlayback(
				new ParamAction<Playback<SourceInfo>>() {
					@Override
					public void execute(Playback<SourceInfo> sourceInfoPlayback) {
						sourceInfoPlayback.doNotRepeat();
					}
				},
				new Action() {
					@Override
					public void execute() {
						LocalMultiplePlayback.this.notifyStateChanged();
					}
				});
	}

	@Override
	public void shuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().shuffle(true).build();
		notifyStateChanged();
	}

	@Override
	public void doNotShuffle() {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().shuffle(false).build();
		notifyStateChanged();
	}

	@Override
	public void simulateError() {
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.simulateError();
			}
		});
	}

	@Override
	public void setPlayerSources(final List<PlayerSource<SourceInfo>> playerSources) {
		currentPlayback(
				new ParamAction<Playback<SourceInfo>>() {
					@Override
					public void execute(Playback<SourceInfo> value) {
						if (releaseStrategy.releaseCurrentPlayback(playerSources, value.getPlaybackState().playerSource)) {
							releaseCurrentPlaybackAndUpdateState();
							savePlayerSourcesAndInitFirst(playerSources);
						} else {
							multiplePlaybackState = multiplePlaybackState.builder().playerSources(playerSources).build();
							notifyStateChanged();
						}
					}
				},
				new Action() {
					@Override
					public void execute() {
						savePlayerSourcesAndInitFirst(playerSources);
					}
				});
	}

	@Override
	public void playPlayerSource(final PlayerSource<SourceInfo> playerSource) {
		boolean isCurrentPlayerSource = getMultiplePlaybackState()
				.currentPlaybackState
				.transform(new Function<PlaybackState<SourceInfo>, Boolean>() {
					@Nullable
					@Override
					public Boolean apply(PlaybackState<SourceInfo> input) {
						return input.playerSource.equals(playerSource);
					}
				})
				.or(false);
		if (isCurrentPlayerSource) {
			Log.w(getClass().getSimpleName(), "Attempt to play already played player source");
			return;
		}
		boolean playerSourceExists = getMultiplePlaybackState().playerSources.contains(playerSource);
		if (!playerSourceExists) {
			Log.w(getClass().getSimpleName(), "Attempt to play not existed player source");
			return;
		}
		releaseCurrentPlaybackAndUpdateState();
		setAndInitAndPlayCurrentPlayback(playerSource);
	}

	@Override
	public void playNextPlayerSource() {
		determineAndPlayPlayerSource(this.nextPlayerSourceDetermineStrategyFactory);
	}

	@Override
	public void playPreviousPlayerSource() {
		determineAndPlayPlayerSource(this.previousPlayerSourceDetermineStrategyFactory);
	}

	private boolean shouldSwitchToNewPlayerSource(PlaybackState<SourceInfo> playbackState) {
		return playbackState.state == State.COMPLETED;
	}

	private void switchToNewPlayerSource() {
		determineAndPlayPlayerSource(this.onCompletePlayerSourceDetermineStrategyFactory);
	}

	private void setAndInitCurrentPlayback(PlayerSource<SourceInfo> playerSource) {
		this.currentPlayback = Optional.of(createPlayback(playerSource));
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.init();
			}
		});
	}

	private void setAndInitAndPlayCurrentPlayback(PlayerSource<SourceInfo> playerSource) {
		setAndInitCurrentPlayback(playerSource);
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> sourceInfoPlayback) {
				sourceInfoPlayback.play();
			}
		});
	}

	private void releaseCurrentPlaybackAndUpdateState() {
		currentPlayback(new ParamAction<Playback<SourceInfo>>() {
			@Override
			public void execute(Playback<SourceInfo> value) {
				value.release();
				LocalMultiplePlayback.this.currentPlayback = Optional.absent();
				LocalMultiplePlayback.this.multiplePlaybackState.builder().currentPlaybackState(Optional.<PlaybackState<SourceInfo>>absent());
			}
		});
	}

	private void currentPlayback(ParamAction<Playback<SourceInfo>> success) {
		currentPlayback(success, NullAction.INSTANCE);
	}

	private void currentPlayback(ParamAction<Playback<SourceInfo>> success, Action fail) {
		if (this.currentPlayback.isPresent()) {
			success.execute(this.currentPlayback.get());
		} else {
			fail.execute();
		}
	}

	private void notifyStateChanged() {
		this.multiplePlaybackListener.onUpdate(this.multiplePlaybackState);
	}

	private Playback<SourceInfo> createPlayback(PlayerSource<SourceInfo> playerSource) {
		Playback<SourceInfo> playback = this.playbackFactory.create(this.context, this.multiplePlaybackState.repeatSingle, playerSource);
		playback.setPlaybackListener(this.playbackListener);
		if (getMultiplePlaybackState().repeatSingle) {
			playback.repeat();
		} else {
			playback.doNotRepeat();
		}
		return playback;
	}

	private void savePlayerSourcesAndInitFirst(List<PlayerSource<SourceInfo>> playerSources) {
		this.multiplePlaybackState = this.multiplePlaybackState.builder().playerSources(playerSources).build();
		boolean hasPlayerSources = !getMultiplePlaybackState().playerSources.isEmpty();
		if (hasPlayerSources) {
			PlayerSource<SourceInfo> playerSource = getMultiplePlaybackState().playerSources.get(0);
			setAndInitCurrentPlayback(playerSource);
		} else {
			notifyStateChanged();
		}
	}

	private void determineAndPlayPlayerSource(final PlayerSourceDetermineStrategyFactory<SourceInfo> strategyFactory) {
		currentPlayback(
				new ParamAction<Playback<SourceInfo>>() {
					@Override
					public void execute(Playback<SourceInfo> playback) {
						Optional<PlayerSource<SourceInfo>> newPlayerSource = strategyFactory.create(getMultiplePlaybackState().shuffle)
								.determine(LocalMultiplePlayback.this.getMultiplePlaybackState().playerSources, playback.getPlaybackState().playerSource);
						if (!newPlayerSource.isPresent()) {
							Log.w(LocalMultiplePlayback.this.getClass().getSimpleName(), "Player source is not determined");
							return;
						}
						LocalMultiplePlayback.this.releaseCurrentPlaybackAndUpdateState();
						LocalMultiplePlayback.this.setAndInitAndPlayCurrentPlayback(newPlayerSource.get());
					}
				},
				new Action() {
					@Override
					public void execute() {
						boolean hasPlayerSources = !LocalMultiplePlayback.this.getMultiplePlaybackState().playerSources.isEmpty();
						if (hasPlayerSources) {
							PlayerSource<SourceInfo> playerSource = LocalMultiplePlayback.this.getMultiplePlaybackState().playerSources.get(0);
							LocalMultiplePlayback.this.setAndInitAndPlayCurrentPlayback(playerSource);
						} else {
							Log.w(LocalMultiplePlayback.this.getClass().getSimpleName(), "There are not player sources to play");
						}
					}
				});
	}

	private final PlaybackListener<SourceInfo> playbackListener = new PlaybackListener<SourceInfo>() {
		@Override
		public void onUpdate(PlaybackState<SourceInfo> playbackState) {
			multiplePlaybackState = multiplePlaybackState
					.builder()
					.currentPlaybackState(Optional.of(playbackState))
					.build();
			notifyStateChanged();
			if (shouldSwitchToNewPlayerSource(playbackState)) {
				switchToNewPlayerSource();
			}
		}

		@Override
		public void onError(Throwable throwable) {
			multiplePlaybackState = multiplePlaybackState
					.builder()
					.currentPlaybackState(currentPlayback.transform(new Function<Playback<SourceInfo>, PlaybackState<SourceInfo>>() {
						@Nullable
						@Override
						public PlaybackState<SourceInfo> apply(Playback<SourceInfo> sourceInfoPlayback) {
							return sourceInfoPlayback.getPlaybackState();
						}
					}))
					.build();
			multiplePlaybackListener.onError(throwable);
			switchToNewPlayerSource();
		}
	};
}
