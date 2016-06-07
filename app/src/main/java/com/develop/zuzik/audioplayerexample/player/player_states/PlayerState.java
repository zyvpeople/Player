package com.develop.zuzik.audioplayerexample.player.player_states;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.PlaybackListener;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/2/16
 */
public interface PlayerState {

	PlayerStateBundle getPlayerStateBundle();

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
