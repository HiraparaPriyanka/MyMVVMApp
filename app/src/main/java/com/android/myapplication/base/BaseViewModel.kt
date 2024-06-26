package com.android.myapplication.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val toastObservable : MutableLiveData<Any?> by lazy { MutableLiveData<Any?>() }
}