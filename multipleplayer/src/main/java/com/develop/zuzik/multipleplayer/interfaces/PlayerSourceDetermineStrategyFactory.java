package com.develop.zuzik.multipleplayer.interfaces;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlayerSourceDetermineStrategyFactory<SourceInfo> extends Serializable {
	PlayerSourceDetermineStrategy<SourceInfo> create(boolean shuffle);
}
