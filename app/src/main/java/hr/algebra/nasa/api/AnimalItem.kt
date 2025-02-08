package hr.algebra.nasa.api

import com.google.gson.annotations.SerializedName

data class AnimalItem(
    @SerializedName("binomialName") val binomialName: String,
    @SerializedName("commonName")   val commonName: String,
    @SerializedName("location")     val location: String,
    @SerializedName("wikiLink")     val wikiLink: String,
    @SerializedName("lastRecord")   val lastRecord: String,
    @SerializedName("imageSrc")     val imageSrc: String,
    @SerializedName("shortDesc")    val shortDesc: String
)
