package com.develop.zuzik.audioplayerexample.player.player_states.interfaces;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.audioplayerexample.player.interfaces.Action;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContext<SourceInfo> {

	MediaPlayer getMediaPlayer();

	PlayerSource<SourceInfo> getPlayerSource();

	boolean isRepeat();

	Context context();

	void setPlayerState(PlayerState playerState);

	void onUpdate();

	void onError(Throwable throwable);

	void requestFocus(Action success, Action fail);

	void abandonAudioFocus();

}