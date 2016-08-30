package com.develop.zuzik.musicbrowser.presentation.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.application.injection.injector.PlaybackListItemViewInjector
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.player.source.PlayerSource
import kotlinx.android.synthetic.main.view_playback_list_item.view.*
import javax.inject.Inject

/**
 * User: zuzik
 * Date: 8/21/16
 */
class PlaybackListItemView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr), MultiplePlayer.ActiveSourceView<Song> {

    @Inject
    lateinit var presenter: MultiplePlayer.ActiveSourcePresenter<Song>

    var song: PlayerSource<Song>? = null
        set(value) {
            //TODO: set image
            this.author.text = value?.sourceInfo?.author
            this.name.text = value?.sourceInfo?.name
            this.presenter.setPlayerSource(value)
        }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null) {
        View.inflate(context, R.layout.view_playback_list_item, this)
        PlaybackListItemViewInjector().inject(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.presenter.setView(this)
        this.presenter.onCreate()
        this.presenter.onAppear()
    }

    override fun onDetachedFromWindow() {
        this.presenter.setView(null)
        this.presenter.onDisappear()
        this.presenter.onDestroy()
        super.onDetachedFromWindow()
    }

    //region MultiplePlayer.ActiveSourceView

    override fun displayAsActiveSource() {
        setBackgroundColor(ContextCompat.getColor(this.context, R.color.playback_item_background_active))
    }

    override fun displayAsInactiveSource() {
        setBackgroundColor(ContextCompat.getColor(this.context, R.color.playback_item_background_not_active))
    }

    //endregion
}