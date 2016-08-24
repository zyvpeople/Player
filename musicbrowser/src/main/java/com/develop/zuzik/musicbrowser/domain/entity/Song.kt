package com.develop.zuzik.musicbrowser.domain.entity

import android.net.Uri

/**
 * User: zuzik
 * Date: 8/21/16
 */
data class Song(
        val author: String,
        val name: String,
        val image: Uri)