package com.android.myapplication.data.services

import com.android.myapplication.model.AllUserModel
import retrofit2.Response
import retrofit2.http.GET

public interface AppService {

    @GET("square/repos")
    suspend fun getAllUser(): Response<List<AllUserModel>>?

}