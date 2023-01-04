package github.io.rahmatsyam.myweatherapp.util

import android.app.Activity
import android.widget.Toast

fun Activity.myToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun kelvinToCelsius(kelvinTemperature: Double?): Int {
    val celsius = "%.0f".format(kelvinTemperature?.minus(273.15))
    return celsius.toInt()
}