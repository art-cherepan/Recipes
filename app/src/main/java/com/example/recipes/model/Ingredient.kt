package com.example.recipes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Ingredient (
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable