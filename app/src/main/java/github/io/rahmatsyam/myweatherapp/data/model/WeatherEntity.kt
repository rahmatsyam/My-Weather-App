package github.io.rahmatsyam.myweatherapp.data.model

import com.squareup.moshi.Json

data class WeatherEntity(

    @Json(name="name")
    val name: String? = null,

    @Json(name="temp")
    val temp: Double = 0.0,

    @Json(name="main")
    val main: String? = null,

    @Json(name="description")
    val description: String? = null




)
