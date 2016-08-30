package com.develop.zuzik.musicbrowser.domain.entity

import java.io.Serializable

/**
 * User: zuzik
 * Date: 8/21/16
 */
data class Song(
        val author: String,
        val name: String,
        val imageUri: String) : Serializable