package com.effectiveapps.data.model

import com.google.gson.annotations.SerializedName
import com.effectiveapps.domain.model.SmartphoneGoodDetails


data class SmartphoneGoodDetailsDto(
    @SerializedName("CPU")
    val cpu: String,
    @SerializedName("camera")
    val camera: String,
    @SerializedName("capacity")
    val capacity: List<String>,
    @SerializedName("color")
    val color: List<String>,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("isFavorites")
    val isFavorites: Boolean,
    @SerializedName("price")
    val price: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("sd")
    val sd: String,
    @SerializedName("ssd")
    val ssd: String,
    @SerializedName("title")
    val title: String
){
    fun toSmartphoneGoodDetails() = SmartphoneGoodDetails(
        cpu, camera, capacity, color, id, images, isFavorites, price, rating, sd, ssd, title
    )
}