package com.jean.postapp.domain.model.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val id: Int,
    val name: String,
    val body: String
) : Parcelable