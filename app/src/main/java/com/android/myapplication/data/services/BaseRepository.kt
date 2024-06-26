package com.android.myapplication.data.services

import com.android.myapplication.R
import com.android.myapplication.data.network.RetrofitBuilder
import com.android.myapplication.utils.Resource
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository {

    var appService: AppService = RetrofitBuilder.appService

    // handle the all exception
    inline fun <reified T> exceptionHandler(
        throwable: Throwable
    ): Resource<T> {
        return when (throwable) {
            is SocketTimeoutException -> {
                Resource.error(R.string.server_timeout, throwable = throwable)
            }
            is ConnectException, is HttpException, is UnknownHostException -> {
                Resource.error(R.string.no_internet_connection, throwable = throwable)
            }
            is ClassCastException -> {
                Resource.error(R.string.something_went_wrong, throwable = throwable)
            }
            is NumberFormatException -> {
                Resource.error(R.string.something_went_wrong, throwable = throwable)
            }
            else -> {
                Resource.error(R.string.something_went_wrong, throwable = throwable)
            }
        }
    }
}
