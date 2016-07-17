package com.develop.zuzik.multipleplayer.interfaces;

import java.io.Serializable;

/**
 * User: zuzik
 * Date: 6/18/16
 */
public interface PlayerSourceStrategyFactory<SourceInfo> extends Serializable {
	PlayerSourceStrategy<SourceInfo> create(boolean shuffle);
}
