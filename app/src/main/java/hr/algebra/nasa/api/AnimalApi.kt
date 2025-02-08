package hr.algebra.nasa.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://extinct-api.herokuapp.com/"

interface AnimalApi {
    @GET("api/v1/animal/")
    fun fetchItems(@Query("count") count: Int = 10) : Call<AnimalResponseDto>
}