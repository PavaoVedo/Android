package hr.algebra.nasa.api

import com.google.gson.annotations.SerializedName

data class AnimalResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("data")   val data: List<AnimalItem>
)