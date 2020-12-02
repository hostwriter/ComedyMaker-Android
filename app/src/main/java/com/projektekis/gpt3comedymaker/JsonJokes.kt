package com.projektekis.gpt3comedymaker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class JsonJokes(val theme: String, val likes: Int, val comments: ArrayList<Comments>, val dislikes: Int, val id: String,
                val title: String, val body: String, val author: String ) : Parcelable

@Parcelize
class Comments(val body: String?, val author: String?) : Parcelable
