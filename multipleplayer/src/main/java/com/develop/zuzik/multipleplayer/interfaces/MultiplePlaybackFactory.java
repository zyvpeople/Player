package com.develop.zuzik.multipleplayer.interfaces;

import android.content.Context;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 7/7/16
 */
public interface MultiplePlaybackFactory<SourceInfo> extends Serializable {

	MultiplePlayback<SourceInfo> create(Context context);
}
