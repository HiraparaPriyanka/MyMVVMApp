package com.android.myapplication.view.home

import android.util.Log
import androidx.viewbinding.ViewBinding
import com.android.myapplication.base.BaseActivity
import com.android.myapplication.data.DataHolder
import com.android.myapplication.databinding.ActivityRepositoryDetailsBinding
import com.android.myapplication.model.AllUserModel


class RepositoryDetailsActivity : BaseActivity() {
    private lateinit var mBinding: ActivityRepositoryDetailsBinding
    private var userData: AllUserModel? = null

    override fun getViewBinder(): ViewBinding {
        mBinding = ActivityRepositoryDetailsBinding.inflate(layoutInflater)
        return mBinding
    }

    override fun initView() {
        userData= DataHolder.getInstance().data
        mBinding.textViewName.text = buildString {
            append("UserName:")
            append(userData?.name)
        }

        mBinding.textViewCount.text = buildString {
            append("Star Count:")
            append(userData?.stargazersCount.toString())
        }
    }

    override fun initObservers() {

    }

}