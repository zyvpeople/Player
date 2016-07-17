package com.develop.zuzik.audioplayerexample.mvp.interfaces;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface MultiplePlayer {
	interface Model<SourceInfo> {
		void setSources(List<PlayerSource<SourceInfo>> sources);

		void clear();

		Optional<MultiplePlaybackState<SourceInfo>> getState();

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
		void repeat();

		void doNotRepeat();

		void shuffle();

		void doNotShuffle();

		void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds);

		void showProgress();

		void hideProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

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

		void onPlay();

		void onPause();

		void onStop();

		void onSkipNext();

		void onSkipPrevious();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeatSingle();

		void onDoNotRepeatSingle();

		void onShuffle();

		void onDoNotShuffle();

		void onRepeatAll();

		void onDoNotRepeatAll();

		void onSwitchToSource(PlayerSource<SourceInfo> source);

		void simulateError();
	}
}
