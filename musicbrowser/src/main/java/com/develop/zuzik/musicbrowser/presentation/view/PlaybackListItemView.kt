package com.develop.zuzik.musicbrowser.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.player.source.PlayerSource
import kotlinx.android.synthetic.main.view_playback_list_item.view.*

/**
 * User: zuzik
 * Date: 8/21/16
 */
class PlaybackListItemView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    var song: PlayerSource<Song>? = null
        set(value) {
            //TODO: set image
            this.author.text = value?.sourceInfo?.author
            this.name.text = value?.sourceInfo?.name
        }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null) {
        View.inflate(context, R.layout.view_playback_list_item, this)
    }

}