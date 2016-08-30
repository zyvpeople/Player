package com.develop.zuzik.musicbrowser.application.injection.module

import android.content.Context
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory
import com.develop.zuzik.multipleplayer.local.LocalMultiplePlaybackFactory
import com.develop.zuzik.multipleplayer.player_source_release_strategy.DoNotReleaseIfExistsPlayerSourceReleaseStrategy
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlaybackSettings
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer
import com.develop.zuzik.multipleplayermvp.model.MultiplePlayerServiceModel
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerActiveSourcePresenter
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerControlPresenter
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerSourcesPresenter
import com.develop.zuzik.multipleplayermvp.presenter_destroy_strategy.DoNothingMultiplePlayerPresenterDestroyStrategy
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.domain.player.*
import com.develop.zuzik.musicbrowser.presentation.player.MultiplePlayerNotificationFactoryImpl
import com.develop.zuzik.musicbrowser.presentation.player.PlayerExceptionMessageProviderImpl
import com.develop.zuzik.player.interfaces.PlaybackFactory
import com.develop.zuzik.player.local.LocalPlaybackFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/24/16
 */
@Singleton
@Module
class PlayerModule {

    @Singleton
    @Provides
    fun playerModel(
            context: Context,
            playbackSettings: MultiplePlaybackSettings,
            multiplePlaybackFactory: MultiplePlaybackFactory<Song>,
            notificationFactory: MultiplePlayerNotificationFactory<Song>): MultiplePlayer.Model<Song> =
            MultiplePlayerServiceModel<Song>(
                    context,
                    playbackSettings,
                    multiplePlaybackFactory,
                    100500,
                    notificationFactory)

    @Singleton
    @Provides
    fun multiplePlaybackFactory(
            playbackFactory: PlaybackFactory<Song>,
            playbackSettings: MultiplePlaybackSettings): MultiplePlaybackFactory<Song> =
            LocalMultiplePlaybackFactory(
                    playbackFactory,
                    NextPlayerSourceDetermineStrategyFactory<Song>(),
                    PreviousPlayerSourceDetermineStrategyFactory<Song>(),
                    OnCompletePlayerSourceDetermineStrategyFactory<Song>(),
                    playbackSettings.isRepeatSingle,
                    playbackSettings.isShuffle)

    @Singleton
    @Provides
    fun playbackFactory(): PlaybackFactory<Song> = LocalPlaybackFactory()

    @Provides
    fun presenter(context: Context, model: MultiplePlayer.Model<Song>): MultiplePlayer.SourcesPresenter<Song> =
            MultiplePlayerSourcesPresenter(
                    model,
                    DoNothingMultiplePlayerPresenterDestroyStrategy(),
                    DoNotReleaseIfExistsPlayerSourceReleaseStrategy(),
                    PlayerExceptionMessageProviderImpl(context))

    @Singleton
    @Provides
    fun notificationFactory(): MultiplePlayerNotificationFactory<Song> = MultiplePlayerNotificationFactoryImpl()

    @Provides
    fun controlPresenter(model: MultiplePlayer.Model<Song>): MultiplePlayer.ControlPresenter<Song> =
            MultiplePlayerControlPresenter(
                    model,
                    NextControlAvailabilityStrategy(),
                    PreviousControlAvailabilityStrategy())

    @Provides
    fun activeSourcePresenter(model: MultiplePlayer.Model<Song>): MultiplePlayer.ActiveSourcePresenter<Song> =
            MultiplePlayerActiveSourcePresenter(model)
}