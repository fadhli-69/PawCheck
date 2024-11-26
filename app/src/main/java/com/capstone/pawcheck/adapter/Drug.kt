package com.capstone.pawcheck.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drug(
    val name: String,
    val description: String,
    val imageResId: Int
) : Parcelable
