package com.android.myapplication.base


import android.app.Application
import com.android.myapplication.data.network.RetrofitBuilder
import io.realm.Realm
import io.realm.RealmConfiguration


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
        Realm.init(applicationContext)

        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(config)
        createService()

    }

    private fun createService() {
        RetrofitBuilder.buildService()
    }

    companion object{
        private var instance: MyApplication? = null

        fun getContext(): MyApplication {
            return instance!!
        }
    }

}

