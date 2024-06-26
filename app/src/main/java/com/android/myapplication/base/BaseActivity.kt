package com.android.myapplication.base


import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.android.myapplication.R
import com.android.myapplication.utils.extentions.SharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getViewBinder(): ViewBinding

    abstract fun initView()

    abstract fun initObservers()

    lateinit var sharedPref: SharedPreferences
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        setContentView(getViewBinder().root)
        sharedPref = SharedPref.getSharedPreference(this)
        initView()
        initObservers()
        initializeProgressDialog()
        val parameters = Bundle().apply {
            this.putString(this::class.java.simpleName,this::class.java.simpleName )
        }
       // val firebaseAnalytics = FirebaseAnalytics.getInstance(this@BaseActivity)
       // firebaseAnalytics.logEvent(this::class.java.simpleName, parameters)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    /**
     *@desc create progress dialog for doing api call or heavy task on main thread
     *@return Dialog
     */
    private fun initializeProgressDialog(): Dialog {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        return progressDialog
    }

    /**
     *@desc show progress dialog when doing api call or heavy task on main thread
     */
    open fun showProgressDialog() {
        if (!(this as Activity).isFinishing) {
            if (::progressDialog.isInitialized && !progressDialog.isShowing)
                progressDialog.show()
        }
    }

    /**
     *@desc Hide progress dialog when doing api call or heavy task on main thread
     */
    fun dismissProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }



    fun showErrorDialog(message: String? = getString(R.string.something_went_wrong)) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.attention)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.create().show()

    }
}