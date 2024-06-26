package com.android.myapplication.data.network

import android.content.SharedPreferences

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(var sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        builder.header("Accept", "application/json")
        builder.header("Content-Type", "application/json")
        request = builder.build() //overwrite old request
        return chain.proceed(request)
    }

}