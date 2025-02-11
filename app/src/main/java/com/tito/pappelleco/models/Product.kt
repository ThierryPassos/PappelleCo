package com.tito.pappelleco.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageResId: Int
) : Parcelable
