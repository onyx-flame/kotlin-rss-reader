package com.onyx.rssreader.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article (
    val title: String,
    val description: String,
    val image: String,
    val pubDate: String,
    val content: String
) : Parcelable
