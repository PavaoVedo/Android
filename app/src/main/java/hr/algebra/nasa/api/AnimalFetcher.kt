package hr.algebra.nasa.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.nasa.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasa.AnimalReceiver
import hr.algebra.nasa.framework.sendBroadcast
import hr.algebra.nasa.handler.downloadImage
import hr.algebra.nasa.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimalFetcher(private val context: Context) {

    private var animalApi: AnimalApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        animalApi = retrofit.create(AnimalApi::class.java)
    }

    fun fetchItems(count: Int) {

        val request = animalApi.fetchItems(count)

        request.enqueue(object: Callback<AnimalResponseDto> {
            override fun onResponse(
                call: Call<AnimalResponseDto>,
                response: Response<AnimalResponseDto>,
            ) {
                val allAnimals = response.body()?.data ?: emptyList()
                populateItems(allAnimals)
            }

            override fun onFailure(call: Call<AnimalResponseDto>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

        })


    }

    private fun populateItems(animalItems: List<AnimalItem>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            animalItems.forEach {
                val picturePath = downloadImage(context, it.imageSrc)
                val values = ContentValues().apply {
                    put(Item::binomialName.name, it.binomialName)
                    put(Item::commonName.name, it.commonName)
                    put(Item::location.name, it.location)
                    put(Item::wikiLink.name, it.wikiLink)
                    put(Item::lastRecord.name, it.lastRecord)
                    put(Item::imageSrc.name, picturePath ?: "")
                    put(Item::shortDesc.name, it.shortDesc)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(
                    NASA_PROVIDER_CONTENT_URI,
                    values
                )



            }
            context.sendBroadcast<AnimalReceiver>()
        }



    }
}