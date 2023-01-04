package github.io.rahmatsyam.myweatherapp.ui

import android.content.Entity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.Api
import github.io.rahmatsyam.myweatherapp.data.model.Resource
import github.io.rahmatsyam.myweatherapp.data.model.WeatherEntity
import github.io.rahmatsyam.myweatherapp.data.model.WeatherResponse
import github.io.rahmatsyam.myweatherapp.data.remote.ApiEndpoint
import github.io.rahmatsyam.myweatherapp.data.repo.WeatherRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherViewModel(private val repo: WeatherRepo) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val mLocalCurrentWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mSingaporeCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mNewYorkCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mMumbaiCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mDelhiCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mSydneyCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private val mMelbourneCityWeatherResponse = MutableLiveData<Resource<WeatherResponse>>()

    private val mLocalCurrentWeather = MutableLiveData<WeatherEntity?>()
    private val mSingaporeCityWeatherSession = MutableLiveData<WeatherEntity?>()
    private val mNewYorkCityWeatherSession = MutableLiveData<WeatherEntity?>()
    private val mMumbaiCityWeatherSession = MutableLiveData<WeatherEntity?>()
    private val mDelhiCityWeatherSession = MutableLiveData<WeatherEntity?>()
    private val mSydneyCityWeatherSession = MutableLiveData<WeatherEntity?>()
    private val mMelbourneCityWeatherSession = MutableLiveData<WeatherEntity?>()

    fun getLocalCurrentWeather(lat: Double, lon: Double, apiKey: String) {
        compositeDisposable.add(
            repo.getLocalCurrentWeather(lat, lon, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mLocalCurrentWeatherResponse.postValue(Resource.success(response))
                }, {
                    Resource.error(it.message.toString(), null)
                })
        )
    }

    fun getCityWeather(q: String, apiKey: String) {
        compositeDisposable.add(
            repo.getCityWeather(q, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (q) {
                        ApiEndpoint.NEW_YORK -> mNewYorkCityWeatherResponse.postValue(
                            Resource.success(
                                it
                            )
                        )

                        ApiEndpoint.SINGAPORE -> mSingaporeCityWeatherResponse.postValue(
                            Resource.success(
                                it
                            )
                        )

                        ApiEndpoint.MUMBAI -> mMumbaiCityWeatherResponse.postValue(
                            Resource.success(
                                it
                            )
                        )

                        ApiEndpoint.DELHI -> mDelhiCityWeatherResponse.postValue(Resource.success(it))

                        ApiEndpoint.SYDNEY -> mSydneyCityWeatherResponse.postValue(
                            Resource.success(
                                it
                            )
                        )

                        ApiEndpoint.MELBOURNE -> mMelbourneCityWeatherResponse.postValue(
                            Resource.success(
                                it
                            )
                        )
                    }

                }, {
                    Resource.error(it.message.toString(), null)
                })

        )
    }

    fun setLocalCurrentWeather(weatherEntity: WeatherEntity?) {
        mLocalCurrentWeather.postValue(weatherEntity)
    }

    fun setLocalCityWeather(city: String, weatherEntity: WeatherEntity?) {
        when (city) {
            ApiEndpoint.NEW_YORK -> mNewYorkCityWeatherSession.postValue(weatherEntity)

            ApiEndpoint.SINGAPORE -> mSingaporeCityWeatherSession.postValue(weatherEntity)

            ApiEndpoint.MUMBAI -> mMumbaiCityWeatherSession.postValue(weatherEntity)

            ApiEndpoint.DELHI -> mDelhiCityWeatherSession.postValue(weatherEntity)

            ApiEndpoint.SYDNEY -> mSydneyCityWeatherSession.postValue(weatherEntity)

            ApiEndpoint.MELBOURNE -> mMelbourneCityWeatherSession.postValue(weatherEntity)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLocalCityWeather(): LiveData<Resource<WeatherResponse>> = mLocalCurrentWeatherResponse

    fun getNYCityWeather(): LiveData<Resource<WeatherResponse>> = mNewYorkCityWeatherResponse

    fun getSGCityWeather(): LiveData<Resource<WeatherResponse>> = mSingaporeCityWeatherResponse

    fun getMBCityWeather(): LiveData<Resource<WeatherResponse>> = mMumbaiCityWeatherResponse

    fun getDHCityWeather(): LiveData<Resource<WeatherResponse>> = mDelhiCityWeatherResponse

    fun getSYCityWeather(): LiveData<Resource<WeatherResponse>> = mSydneyCityWeatherResponse

    fun getMNCityWeather(): LiveData<Resource<WeatherResponse>> = mMelbourneCityWeatherResponse

    fun getLocalWeatherSession(): LiveData<WeatherEntity?> = mLocalCurrentWeather

    fun getNYCityWeatherSession(): LiveData<WeatherEntity?> = mNewYorkCityWeatherSession

    fun getSGCityWeatherSession(): LiveData<WeatherEntity?> = mSingaporeCityWeatherSession

    fun getMBCityWeatherSession(): LiveData<WeatherEntity?> = mMumbaiCityWeatherSession

    fun getDHCityWeatherSession(): LiveData<WeatherEntity?> = mDelhiCityWeatherSession

    fun getSYCityWeatherSession(): LiveData<WeatherEntity?> = mSydneyCityWeatherSession

    fun getMNCityWeatherSession(): LiveData<WeatherEntity?> = mMelbourneCityWeatherSession


}