package github.io.rahmatsyam.myweatherapp.data.remote


import github.io.rahmatsyam.myweatherapp.data.model.WeatherResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET(ApiEndpoint.WEATHER)
    fun getLocalCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
    ): Observable<WeatherResponse>

    @GET(ApiEndpoint.WEATHER)
    fun getCityWeather(
        @Query("q") q: String,
        @Query("appid") apiKey: String
    ): Observable<WeatherResponse>

}
