package com.android.myapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.myapplication.base.BaseViewModel
import com.android.myapplication.repository.UserRepository
import com.android.myapplication.utils.Resource
import com.android.myapplication.model.AllUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private var userRepository: UserRepository) : BaseViewModel() {

    val userObservable = MutableLiveData<Resource<AllUserModel>>()

    // get all user using repository
    fun getAllUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                userObservable.postValue(Resource.loading())
                userObservable.postValue(userRepository.getAllUser())
            }
        }
    }

}