package github.io.rahmatsyam.myweatherapp.data.remote

class ApiHelper(private val apiService: ApiServices) {

    fun getCurrentLocalWeather(lat: Double, lon: Double, apiKey: String) =
        apiService.getLocalCurrentWeather(lat, lon, apiKey)

    fun getCityWeather(q: String, apiKey: String) = apiService.getCityWeather(q, apiKey)
}