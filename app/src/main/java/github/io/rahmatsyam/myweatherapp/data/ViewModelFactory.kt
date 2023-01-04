package github.io.rahmatsyam.myweatherapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import github.io.rahmatsyam.myweatherapp.data.remote.ApiHelper
import github.io.rahmatsyam.myweatherapp.data.repo.WeatherRepo
import github.io.rahmatsyam.myweatherapp.ui.WeatherViewModel

class ViewModelFactory(private val api: ApiHelper) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> WeatherViewModel(
                WeatherRepo(api)
            ) as T
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}