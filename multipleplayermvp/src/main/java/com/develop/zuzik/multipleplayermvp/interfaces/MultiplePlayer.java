package com.develop.zuzik.multipleplayermvp.interfaces;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceReleaseStrategy;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {

	interface Model<SourceInfo> {
		void setSources(List<PlayerSource<SourceInfo>> sources, PlayerSourceReleaseStrategy<SourceInfo> releaseStrategy);

		void clear();

		Optional<MultiplePlaybackState<SourceInfo>> getState();

		void videoViewSetter(ParamAction<VideoViewSetter> success);

		void addListener(Listener<SourceInfo> listener);

		void removeListener(Listener<SourceInfo> listener);

		void play();

		void pause();

		void stop();

		void skipNext();

		void skipPrevious();

		void seekToPosition(int positionInMilliseconds);

		void repeatSingle();

		void doNotRepeatSingle();

		void shuffle();

		void doNotShuffle();

		void switchToSource(PlayerSource<SourceInfo> source);

		void simulateError();

		interface Listener<SourceInfo> {
			void onUpdate(MultiplePlaybackState<SourceInfo> state);

			void onError(Throwable error);
		}
	}

	interface View<SourceInfo> {

		void showError(String message);

		void displayCurrentSource(PlayerSource<SourceInfo> source);

		void doNotDisplayCurrentSource();

		void displaySources(List<PlayerSource<SourceInfo>> playerSources);
	}

	interface Presenter<SourceInfo> {
		void setView(View<SourceInfo> view);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onSetPlayerSources(List<PlayerSource<SourceInfo>> playerSources);

		void onSwitchToSource(PlayerSource<SourceInfo> source);

		void simulateError();

	}

	interface ActiveSourceView<SourceInfo> {

		void displayAsActiveSource();

		void displayAsInactiveSource();

	}

	interface ActiveSourcePresenter<SourceInfo> {
		void setView(ActiveSourceView<SourceInfo> view);

		void setPlayerSource(PlayerSource<SourceInfo> playerSource);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();
	}

	interface ControlView<SourceInfo> {
		void repeat();

		void doNotRepeat();

		void shuffle();

		void doNotShuffle();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showProgress();

		void hideProgress();

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void enableSwitchControls(boolean next, boolean previous);
	}

	interface ControlPresenter<SourceInfo> {
		void setView(ControlView<SourceInfo> view);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onPlay();

		void onPause();

		void onStop();

		void onSkipNext();

		void onSkipPrevious();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void onShuffle();

		void onDoNotShuffle();

	}

	interface VideoView<SourceInfo> {

		void setVideoViewAvailable();

		void setVideoViewUnavailable();

		void setVideoView(VideoViewSetter setter);

		void clearVideoView(VideoViewSetter setter);
	}

	interface VideoPresenter<SourceInfo> {
		void setView(VideoView<SourceInfo> view);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onVideoViewCreated();

		void onVideoViewDestroyed();
	}
}
