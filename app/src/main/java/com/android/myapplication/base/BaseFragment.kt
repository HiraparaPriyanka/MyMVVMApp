package com.android.myapplication.base

import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.android.myapplication.R
import com.android.myapplication.utils.extentions.SharedPref



abstract class BaseFragment : Fragment() {

    private lateinit var progressDialog: Dialog
    lateinit var sharedPref: SharedPreferences
    private var view_ : View? = null


    abstract fun getViewBinder(): ViewBinding

    abstract fun observers()

    abstract fun onViewCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPref.getSharedPreference(requireActivity())
        initializeProgressDialog()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(view_ == null) {
            view_ = getViewBinder().root
        }
        observers()
        return view_
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        val parameters = Bundle().apply {
            this.putString(this::class.java.simpleName,this::class.java.simpleName )
        }
//        val firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
//        firebaseAnalytics.logEvent(this::class.java.simpleName, parameters)
    }

    /**
     *@desc create progress dialog for doing api call or heavy task on main thread
     *@return Dialog
     */
    private fun initializeProgressDialog(isCancelable: Boolean = false): Dialog {
        progressDialog = Dialog(requireActivity())
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(isCancelable)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        return progressDialog
    }



    /**
     *@desc show progress dialog when doing api call or heavy task on main thread
     */
    open fun showProgressDialog() {
        if (!(requireContext() as Activity).isFinishing) {
            if (::progressDialog.isInitialized && !progressDialog.isShowing)
                progressDialog.show()
        }
    }


    /**
     * Hide progress bar when doing api call or heavy task on main thread
     */
    fun dismissProgressDialog() {
        if (::progressDialog.isInitialized &&  progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }



    fun initBackPressed(){
        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}