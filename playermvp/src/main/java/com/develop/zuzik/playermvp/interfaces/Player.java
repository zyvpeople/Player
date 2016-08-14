package com.develop.zuzik.playermvp.interfaces;

import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public interface Player {
	interface Model<SourceInfo> {
		void setSource(PlayerSource<SourceInfo> source);

		void clear();

		Optional<PlaybackState<SourceInfo>> getState();

		void videoViewSetter(ParamAction<VideoViewSetter> success);

		void addListener(Listener<SourceInfo> listener);

		void removeListener(Listener<SourceInfo> listener);

		void play();

		void pause();

		void stop();

		void seekToPosition(int positionInMilliseconds);

		void repeat();

		void doNotRepeat();

		void simulateError();

		interface Listener<SourceInfo> {
			void onUpdate(PlaybackState<SourceInfo> state);

			void onError(Throwable error);
		}
	}

	interface View<SourceInfo> {
		void setRepeat();

		void setDoNotRepeat();

		void setProgress(int totalTimeInMilliseconds, int currentTimeInMilliseconds);

		void showSourceProgress();

		void hideSourceProgress();

		void showTime(String currentTime, String totalTime);

		void showError(String message);

		void enablePlayControls(boolean play, boolean pause, boolean stop);

		void showLoadingIndicator();

		void hideLoadingIndicator();

		void displayCurrentSource(SourceInfo sourceInfo);

		void doNotDisplayCurrentSource();

	}

	interface Presenter<SourceInfo> {
		void setView(View<SourceInfo> view);

		void onCreate();

		void onDestroy();

		void onAppear();

		void onDisappear();

		void onSetSource(PlayerSource<SourceInfo> source);

		void onPlay();

		void onPause();

		void onStop();

		void onSeekToPosition(int positionInMilliseconds);

		void onRepeat();

		void onDoNotRepeat();

		void simulateError();
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
