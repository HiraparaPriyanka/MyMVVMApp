package com.android.myapplication.data.network

import android.util.Base64
import com.bumptech.glide.load.Key
import com.android.myapplication.data.services.AppService
import com.android.myapplication.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
//            .also {
//                it.addInterceptor(AuthorizationInterceptor(sharedPref))
//            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    lateinit var appService: AppService
    fun buildService(){
        appService = getRetrofit().create(AppService::class.java)
    }

    private fun getGetUrl(str: String): String {
        val decode = Base64.decode(str, Base64.DEFAULT)
        try {
            val forName = Charset.forName(Key.STRING_CHARSET_NAME)
            return String(decode, forName)
        } catch (e2: UnsupportedEncodingException) {
        } catch (unused: Throwable) {
        }
        return ""
    }
}