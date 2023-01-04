package github.io.rahmatsyam.myweatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.core.content.edit
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import github.io.rahmatsyam.myweatherapp.data.model.WeatherEntity

class PreferenceManager constructor(private val context: Context) {

    private fun preference(): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE)
    }

    val editor: SharedPreferences.Editor = preference().edit()

    fun saveLocalWeather(model: WeatherEntity?) {
        preference().edit {
            putString(LOCAL_WEATHER, jsonAdapterWeather().toJson(model))
        }
    }

    fun getLocalWeather(): WeatherEntity? {
        val result = preference().getString(LOCAL_WEATHER, null)
        if (!TextUtils.isEmpty(result)) {
            return jsonAdapterWeather().fromJson(result ?: "")
        }
        return null
    }

    fun saveCityWeather(keyCity: String, model: WeatherEntity?) {
        preference().edit {
            putString(keyCity, jsonAdapterWeather().toJson(model))
        }
    }

    fun getCityWeather(keyCity: String): WeatherEntity? {
        val result = preference().getString(keyCity, null)
        if (!TextUtils.isEmpty(result)) {
            return jsonAdapterWeather().fromJson(result ?: "")
        }
        return null
    }

    private fun jsonAdapterWeather(): JsonAdapter<WeatherEntity> {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(WeatherEntity::class.java)
    }

    companion object {
        private const val KEY_PREFERENCE = "github.io.rahmatsyam.myweatherapp"
        private const val LOCAL_WEATHER = "local weather"
        private const val CITY_WEATHER = "city weather"
    }
}