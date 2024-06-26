package com.android.myapplication.view.home

import android.util.Log
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.android.myapplication.R
import com.android.myapplication.adapter.home.UserAdapter
import com.android.myapplication.base.BaseActivity
import com.android.myapplication.data.DataHolder
import com.android.myapplication.databinding.ActivityMainBinding
import com.android.myapplication.interfaces.RecyclerViewItemInterface
import com.android.myapplication.model.AllUserModel
import com.android.myapplication.model.AllUserRealm
import com.android.myapplication.repository.UserRepository
import com.android.myapplication.utils.Status
import com.android.myapplication.utils.extentions.getViewModel
import com.android.myapplication.utils.extentions.showToast
import com.android.myapplication.utils.extentions.startActivityInlineWithAnimation
import com.android.myapplication.viewModel.UserViewModel
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults


class MainActivity : BaseActivity(), RecyclerViewItemInterface {

    private lateinit var viewModel: UserViewModel
    private lateinit var mBinding: ActivityMainBinding
    private var getAllUserList: ArrayList<AllUserModel> = arrayListOf()
    private var getAllUserRealmList: RealmList<AllUserModel> = RealmList()
    private var mRealm: Realm? = null
    private var mUserAdapter: UserAdapter? = null
    private var pos = 0

    override fun getViewBinder(): ViewBinding {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        return mBinding
    }

    override fun initView() {
        viewModel = getViewModel { UserViewModel(UserRepository()) }
        mRealm = Realm.getDefaultInstance()

        mUserAdapter = UserAdapter(R.layout.layout_user_item)
        mUserAdapter!!.setItemClickListener(this)
        mBinding.recyclerViewAllUser.adapter = mUserAdapter

        readAllUserRealm()

    }

    //real all user data from realm database
    private fun readAllUserRealm() {
        mRealm!!.executeTransaction { realm ->
            val results: RealmResults<AllUserRealm> =
                realm.where(AllUserRealm::class.java).findAll()
            if (results.isNullOrEmpty()) {
                viewModel.getAllUserList()
            } else {
                for (employee in results) {
                    if (employee.allUserRealmList.isNullOrEmpty()) {
                        viewModel.getAllUserList()
                    } else {
                        getAllUserRealmList.clear()
                        getAllUserList.clear()
                        getAllUserRealmList = employee.allUserRealmList
                        getAllUserList.addAll(getAllUserRealmList)
                        mUserAdapter?.addAll(getAllUserList)
                    }
                }
            }

        }
    }

    override fun initObservers() {
        viewModel.userObservable.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showProgressDialog()
                    }

                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        getAllUserList.clear()
                        getAllUserList =
                            Gson().fromJson(Gson().toJson(it.data), Array<AllUserModel>::class.java)
                                .toList() as ArrayList<AllUserModel>
                        saveAllUserRealm()

                    }

                    Status.ERROR -> {
                        val error =
                            resource.message?.let { it } ?: getString(resource.resId?.let { it }!!)
                            ?: getString(R.string.something_went_wrong)
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        dismissProgressDialog()
                        // show empty data and view gone
                    }

                }
            }
        }
    }

    private fun saveAllUserRealm() {
        try {
            mRealm?.executeTransactionAsync { realm ->
                val alluser: RealmResults<AllUserRealm> = realm.where(
                    AllUserRealm::class.java
                ).findAll()
                if (alluser.size > 0) {
                    alluser.deleteAllFromRealm()
                }
                try {
                    var allUserRealm =
                        AllUserRealm()
                    allUserRealm = realm.createObject(AllUserRealm::class.java)
                    allUserRealm.allUserRealmList.deleteAllFromRealm()
                    allUserRealm.allUserRealmList.addAll(getAllUserList)

                    getAllUserRealmList.addAll(getAllUserList)

                    if (getAllUserRealmList.size > 0) {
                        readAllUserRealm()
                    }
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
            Log.i("TAG", "saveAllUserRealm:33 " + e.message)
        }

    }


    override fun OnItemClick(position: Int) {
        DataHolder.getInstance().data = getAllUserRealmList[position]
        startActivityInlineWithAnimation<RepositoryDetailsActivity>()
    }

    override fun OnItemBookMarkClick(position: Int, id: Int) {
        mRealm?.executeTransaction(Realm.Transaction { realm ->
            var allUserModel =
                mRealm?.where(AllUserModel::class.java)?.equalTo("id", id)
                    ?.findFirst()

            allUserModel?.bookmark = true
        })

        "Bookmark Added".showToast(this)
        mUserAdapter?.notifyDataSetChanged()
    }

    override fun OnItemUnBookMarkClick(position: Int, id: Int) {
        mRealm?.executeTransaction(Realm.Transaction { realm ->
            var allUserModel =
                mRealm?.where(AllUserModel::class.java)?.equalTo("id", id)
                    ?.findFirst()
            allUserModel?.bookmark = false
        })

        mUserAdapter?.notifyDataSetChanged()
        "Bookmark Remove".showToast(this)

    }

}