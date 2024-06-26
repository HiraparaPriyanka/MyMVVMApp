package com.android.myapplication.repository

import com.android.myapplication.data.services.BaseRepository
import com.android.myapplication.utils.AppConstants
import com.android.myapplication.utils.Resource
import com.android.myapplication.model.AllUserModel

class UserRepository : BaseRepository() {

    suspend fun getAllUser() : Resource<AllUserModel> {
        return try {
            val res = appService.getAllUser()
            when (res?.code()) {
                AppConstants.API_SUCCESS_CODE -> Resource.success(res.body()!!)
                else -> Resource.error(message = res?.errorBody()?.toString())
            }
        } catch (e:Exception){
            exceptionHandler(e)
        }as Resource<AllUserModel>
    }
}