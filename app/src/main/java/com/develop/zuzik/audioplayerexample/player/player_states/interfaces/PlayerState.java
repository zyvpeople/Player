package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	PlaybackState getPlaybackState();

	void setRepeat(boolean repeat);

	//TODO: rename to apply unapply
	void apply(Context context, MediaPlayer player, PlayerInitializer playerInitializer, PlayerStateContainer playerStateContainer, boolean repeat);

	void unapply();

	void play();

	void pause();

	void stop();

	void simulateError();

	void seekTo(int positionInMilliseconds);

	void release();

}
