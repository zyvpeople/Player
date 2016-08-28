package com.develop.zuzik.multipleplayermvp.presenter_destroy_strategy;

import com.develop.zuzik.multipleplayer.player_source_release_strategy.ReleasePlayerSourceReleaseStrategy;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.player.source.PlayerSource;

import java.util.ArrayList;

/**
 * User: zuzik
 * Date: 7/4/16
 */
public class ClearSourcesMultiplePlayerPresenterDestroyStrategy<SourceInfo> implements MultiplePlayerPresenterDestroyStrategy<SourceInfo> {
	@Override
	public void onDestroy(MultiplePlayer.Model<SourceInfo> model) {
		model.setSources(new ArrayList<PlayerSource<SourceInfo>>(), new ReleasePlayerSourceReleaseStrategy<SourceInfo>());
	}
}
