package github.io.rahmatsyam.myweatherapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import github.io.rahmatsyam.myweatherapp.data.ViewModelFactory
import github.io.rahmatsyam.myweatherapp.data.local.PreferenceManager
import github.io.rahmatsyam.myweatherapp.data.model.Status
import github.io.rahmatsyam.myweatherapp.data.model.WeatherEntity
import github.io.rahmatsyam.myweatherapp.data.model.toWeatherEntity
import github.io.rahmatsyam.myweatherapp.data.remote.ApiEndpoint
import github.io.rahmatsyam.myweatherapp.data.remote.ApiHelper
import github.io.rahmatsyam.myweatherapp.data.remote.ApiRepo
import github.io.rahmatsyam.myweatherapp.databinding.ActivityMainBinding
import github.io.rahmatsyam.myweatherapp.ui.WeatherViewModel
import github.io.rahmatsyam.myweatherapp.util.kelvinToCelsius
import github.io.rahmatsyam.myweatherapp.util.myToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var preferenceManager: PreferenceManager
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private val permissionId = 2
    private var lat = 0.0
    private var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initData()
        initObserver()
        initView()
        initEvent()
        getLocation()


    }

    private fun initData() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiRepo.apiServices))
        )[WeatherViewModel::class.java]

        initApi()

    }

    private fun initObserver() {
        viewModel.getLocalCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveLocalWeather(entity)
                    val localData = preferenceManager.getLocalWeather()
                    viewModel.setLocalCurrentWeather(localData)
                    binding.swipeRefresh.isRefreshing = false
                }

                Status.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                }

                Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        viewModel.getNYCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.NEW_YORK, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.NEW_YORK)
                    viewModel.setLocalCityWeather(ApiEndpoint.NEW_YORK, localData)

                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getSGCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.SINGAPORE, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.SINGAPORE)
                    viewModel.setLocalCityWeather(ApiEndpoint.SINGAPORE, localData)

                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getMBCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.MUMBAI, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.MUMBAI)
                    viewModel.setLocalCityWeather(ApiEndpoint.MUMBAI, localData)

                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getDHCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.DELHI, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.DELHI)
                    viewModel.setLocalCityWeather(ApiEndpoint.DELHI, localData)

                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getSYCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.SYDNEY, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.SYDNEY)
                    viewModel.setLocalCityWeather(ApiEndpoint.SYDNEY, localData)
                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getMNCityWeather().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val entity = it.data?.toWeatherEntity()
                    preferenceManager.saveCityWeather(ApiEndpoint.MELBOURNE, entity)
                    val localData = preferenceManager.getCityWeather(ApiEndpoint.MELBOURNE)
                    viewModel.setLocalCityWeather(ApiEndpoint.MELBOURNE, localData)
                }

                Status.ERROR -> {}

                Status.LOADING -> {}
            }
        }

        viewModel.getLocalWeatherSession().observe(this) {
            viewCurrentLocal(it)
        }

        viewModel.getNYCityWeatherSession().observe(this) {
            viewNewYorkWeather(it)
        }

        viewModel.getSGCityWeatherSession().observe(this) {
            viewSingaporeWeather(it)
        }

        viewModel.getMBCityWeatherSession().observe(this) {
            viewMumbaiWeather(it)
        }

        viewModel.getDHCityWeatherSession().observe(this) {
            viewDelhiWeather(it)
        }

        viewModel.getSYCityWeatherSession().observe(this) {
            viewSydneyWeather(it)
        }

        viewModel.getMNCityWeatherSession().observe(this) {
            viewMelbourneWeather(it)
        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                myToast("Rejected")
            }
        }
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) {
                    lat = it.latitude
                    lon = it.longitude
                    viewModel.getLocalCurrentWeather(lat, lon, BuildConfig.API_KEY)

                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun initView() {
        viewCurrentLocal(preferenceManager.getLocalWeather())
        viewNewYorkWeather(preferenceManager.getCityWeather(ApiEndpoint.NEW_YORK))
        viewSingaporeWeather(preferenceManager.getCityWeather(ApiEndpoint.SINGAPORE))
        viewMumbaiWeather(preferenceManager.getCityWeather(ApiEndpoint.MUMBAI))
        viewDelhiWeather(preferenceManager.getCityWeather(ApiEndpoint.DELHI))
        viewSydneyWeather(preferenceManager.getCityWeather(ApiEndpoint.SYDNEY))
        viewMelbourneWeather(preferenceManager.getCityWeather(ApiEndpoint.MELBOURNE))

    }

    private fun viewCurrentLocal(model: WeatherEntity?) {
        binding.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvTempCelsius.text = celsius
            tvMain.text = model?.main
            tvDesc.text = model?.description
            tvCurrentCity.text = model?.name
        }
    }

    private fun viewNewYorkWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvNyCelsiusTemp.text = celsius
            tvMainNy.text = model?.main
            tvDetailNy.text = model?.description
        }
    }

    private fun viewSingaporeWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvSgCelsiusTemp.text = celsius
            tvMainSg.text = model?.main
            tvDetailSg.text = model?.description
        }
    }

    private fun viewMumbaiWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvMbCelsiusTemp.text = celsius
            tvMainMb.text = model?.main
            tvDetailMb.text = model?.description
        }

    }

    private fun viewDelhiWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvDhCelsiusTemp.text = celsius
            tvMainDh.text = model?.main
            tvDetailDh.text = model?.description
        }

    }

    private fun viewSydneyWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvSyCelsiusTemp.text = celsius
            tvMainSy.text = model?.main
            tvDetailSy.text = model?.description
        }

    }

    private fun viewMelbourneWeather(model: WeatherEntity?) {
        binding.lyCity.apply {
            val celsius =
                kelvinToCelsius(model?.temp).toString() + TEMP_CELSIUS
            tvMnCelsiusTemp.text = celsius
            tvMainMn.text = model?.main
            tvDetailMn.text = model?.description
        }
    }

    private fun initApi() {
        viewModel.getCityWeather(ApiEndpoint.NEW_YORK, BuildConfig.API_KEY)
        viewModel.getCityWeather(ApiEndpoint.SINGAPORE, BuildConfig.API_KEY)
        viewModel.getCityWeather(ApiEndpoint.MUMBAI, BuildConfig.API_KEY)
        viewModel.getCityWeather(ApiEndpoint.DELHI, BuildConfig.API_KEY)
        viewModel.getCityWeather(ApiEndpoint.SYDNEY, BuildConfig.API_KEY)
        viewModel.getCityWeather(ApiEndpoint.MELBOURNE, BuildConfig.API_KEY)
    }

    private fun initEvent() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getLocalCurrentWeather(lat, lon, BuildConfig.API_KEY)
            initApi()
        }
    }


    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {
        private const val TEMP_CELSIUS = "°C"
    }


}