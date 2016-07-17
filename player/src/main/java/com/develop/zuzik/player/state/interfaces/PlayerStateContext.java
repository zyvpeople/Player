package com.develop.zuzik.player.state.interfaces;

import android.content.Context;
import android.media.MediaPlayer;

import com.develop.zuzik.player.device_sleep.DeviceSleep;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.source.PlayerSource;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public interface PlayerStateContext<SourceInfo> {

	MediaPlayer getMediaPlayer();

	PlayerSource<SourceInfo> getPlayerSource();

	DeviceSleep getDeviceSleep();

	boolean isRepeat();

	Context context();

	void setPlayerState(PlayerState playerState);

	void onUpdate();

	void onError(Throwable throwable);

	void requestFocus(Action success, Action fail);

	void abandonAudioFocus();

}