package github.io.rahmatsyam.myweatherapp.data.model

import com.squareup.moshi.Json

data class WeatherResponse(

    @Json(name = "visibility")
    val visibility: Int? = null,

    @Json(name = "timezone")
    val timezone: Int? = null,

    @Json(name = "main")
    val main: Main? = null,

    @Json(name = "clouds")
    val clouds: Clouds? = null,

    @Json(name = "sys")
    val sys: Sys? = null,

    @Json(name = "dt")
    val dt: Int? = null,

    @Json(name = "coord")
    val coord: Coord? = null,

    @Json(name = "weather")
    val weather: List<Weather>? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "cod")
    val cod: Int? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "base")
    val base: String? = null,

    @Json(name = "wind")
    val wind: Wind? = null
)

data class Clouds(

    @Json(name = "all")
    val all: Int? = null
)

data class Coord(

    @Json(name = "lon")
    val lon: Any? = null,

    @Json(name = "lat")
    val lat: Any? = null
)

data class Main(

    @Json(name = "temp")
    val temp: Double = 0.0,

    @Json(name = "temp_min")
    val tempMin: Any? = null,

    @Json(name = "grnd_level")
    val grndLevel: Int? = null,

    @Json(name = "humidity")
    val humidity: Int? = null,

    @Json(name = "pressure")
    val pressure: Int? = null,

    @Json(name = "sea_level")
    val seaLevel: Int? = null,

    @Json(name = "feels_like")
    val feelsLike: Any? = null,

    @Json(name = "temp_max")
    val tempMax: Any? = null
)

data class Weather(

    @Json(name = "icon")
    val icon: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "main")
    val main: String? = null,

    @Json(name = "id")
    val id: Int? = null
)

data class Wind(

    @Json(name = "deg")
    val deg: Int? = null,

    @Json(name = "speed")
    val speed: Any? = null,

    @Json(name = "gust")
    val gust: Any? = null
)

data class Sys(

    @Json(name = "country")
    val country: String? = null,

    @Json(name = "sunrise")
    val sunrise: Int? = null,

    @Json(name = "sunset")
    val sunset: Int? = null
)

fun WeatherResponse.toWeatherEntity() = WeatherEntity(
    name = name,
    temp = main?.temp ?: 0.0,
    main = weather?.get(0)?.main,
    description = weather?.get(0)?.description

)


