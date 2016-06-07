package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	void setPlayer(MediaPlayer player);

	void setPlayerInitializer(PlayerInitializer playerInitializer);

	void setPlayerStateContainer(PlayerStateContainer playerStateContainer);

	PlaybackState getPlayerStateBundle();

	void setPlaybackListener(PlaybackListener playbackListener);

	void setRepeat(boolean repeat);

	void set(Context context);

	void unset();

	void play();

	void pause();

	void stop();

	void simulateError();

	void seekTo(int positionInMilliseconds);

	void release();

}
