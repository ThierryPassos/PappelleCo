package com.tito.pappelleco.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val product: Product,
    var quantity: Int = 1
) : Parcelable
