package com.jean.postapp.domain.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Author(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val website: String
) : Parcelable