package com.develop.zuzik.multipleplayer.interfaces;

import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.List;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public interface MultiplePlayback<SourceInfo> {

	MultiplePlaybackState<SourceInfo> getMultiplePlaybackState();

	void videoViewSetter(ParamAction<VideoViewSetter> success);

	void setMultiplePlaybackListener(MultiplePlaybackListener<SourceInfo> multiplePlaybackListener);

	void clear();

	void play();

	void pause();

	void stop();

	void seekTo(int positionInMilliseconds);

	void repeatSingle();

	void doNotRepeatSingle();

	void shuffle();

	void doNotShuffle();

	void simulateError();

	void setPlayerSources(List<PlayerSource<SourceInfo>> playerSources, PlayerSourceReleaseStrategy<SourceInfo> releaseStrategy);

	void playPlayerSource(PlayerSource<SourceInfo> playerSource);

	void playNextPlayerSource();

	void playPreviousPlayerSource();

}
