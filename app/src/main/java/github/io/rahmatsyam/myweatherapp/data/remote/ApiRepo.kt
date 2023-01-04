package github.io.rahmatsyam.myweatherapp.data.remote

import android.util.Log
import com.squareup.moshi.Moshi

import github.io.rahmatsyam.myweatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiRepo {

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor { message ->
            Log.e("Log API", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)

    }.build()

    private val moshi = Moshi.Builder()
//        .add(KotlinJson)
//        .add(KotlinJs)
        .build()

    val apiServices: ApiServices by lazy {
        getRetrofit().create(ApiServices::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return retrofitBuilder()
    }

    private fun retrofitBuilder(): Retrofit {

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}