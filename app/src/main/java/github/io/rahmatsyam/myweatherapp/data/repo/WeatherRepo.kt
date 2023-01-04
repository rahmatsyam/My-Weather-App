package github.io.rahmatsyam.myweatherapp.data.repo

import github.io.rahmatsyam.myweatherapp.data.model.WeatherResponse
import github.io.rahmatsyam.myweatherapp.data.remote.ApiHelper
import io.reactivex.rxjava3.core.Observable

class WeatherRepo(private val api: ApiHelper) {

    fun getLocalCurrentWeather(lat: Double, lon: Double, apiKey: String): Observable<WeatherResponse> =
        api.getCurrentLocalWeather(lat, lon, apiKey)

    fun getCityWeather(q: String, apiKey: String): Observable<WeatherResponse> =
        api.getCityWeather(q, apiKey)
}