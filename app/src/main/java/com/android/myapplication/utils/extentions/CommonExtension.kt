package com.android.myapplication.utils.extentions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.myapplication.R
import com.bumptech.glide.Glide
import com.android.myapplication.base.BaseViewModelFactory
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.jvm.internal.Intrinsics


@Suppress("DEPRECATION")
inline val Context.isOnline: Boolean
    get() {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let { connectivityManager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                    return it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                }
            } else {
                try {
                    connectivityManager.activeNetworkInfo?.let {
                        if (it.isConnected && it.isAvailable) {
                            return true
                        }
                    }
                } catch (e: Exception) {
                }
            }
        }
        return false
    }

fun waitFor(milli: Long, onFinishDelay: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        onFinishDelay()
    }, milli)
}

inline fun <reified T : Activity> Activity.startActivityInline(
    bundle: Bundle? = null,
    finish: Boolean = false,
) {

    startActivity(bundle?.let {
        Intent(this, T::class.java).putExtras(it)
    } ?: Intent(this, T::class.java))

    if (finish) {
        finish()
    }
}

/**
 * method will use for hide keyboard and pass view
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * method will use for hide keyboard
 * @param view - current focus view in activity
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}



inline fun <reified T : Activity> Activity.startActivityInlineWithAnimation(
    bundle: Bundle? = null,
    finish: Boolean = false,
    resultCode: Int? = null
) {
    if (resultCode != null) {
        startActivityForResult(bundle?.let {
            Intent(this, T::class.java).putExtras(it)
        } ?: Intent(this, T::class.java), resultCode)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    } else {
        startActivity(bundle?.let {
            Intent(this, T::class.java).putExtras(it)
        } ?: Intent(this, T::class.java))
    }


    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

    if (finish) {
        finish()
    }
}

/**
 * method will use for show tost message
 */
fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun Window.setFullScreen() {
    WindowCompat.setDecorFitsSystemWindows(this, false)
    WindowInsetsControllerCompat(this, this.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Window.setFullTransScreen() {
    this.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    this.statusBarColor = Color.TRANSPARENT
}


/**
 * method will use for make view click
 */

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

/**
 * method will use for make string to spannable and handle spannable text click
 * @param context - activity or fragment context
 * @param content - spannable text
 * @param startIndex - spannable text start index
 * @param lastIndex -spannable text last index
 * @param onClickListener - spannable text click
 */
fun TextView.makeSpannableString(
    context: Context,
    content: String,
    startIndex: Int,
    lastIndex: Int,
    onClickListener: ClickableSpan? = null,
    isPrivacy: Boolean = false,
) {
    val myString = SpannableString(content)
//    val typeface = context.lightFont()
    myString.setSpan(
        onClickListener,
        startIndex,
        lastIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    myString.setSpan(
        StyleSpan(Typeface.NORMAL),
        startIndex,
        lastIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

//    if (isPrivacy) {
//        myString.setSpan(
//            ForegroundColorSpan(context.getColorCompat(R.color.appTextColor)),
//            startIndex,
//            lastIndex,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//    } else {
//        myString.setSpan(
//            ForegroundColorSpan(context.getColorCompat(R.color.appTextColor)),
//            startIndex,
//            lastIndex,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//    }

    this.text = myString
    this.highlightColor = Color.TRANSPARENT
    this.movementMethod = LinkMovementMethod.getInstance()


}

/**
 * method will use for make string to spannable and handle spannable text click
 * @param context - activity or fragment context
 * @param content - spannable text
 * @param startIndex - spannable text start index
 * @param lastIndex -spannable text last index
 * @param onClickListener - spannable text click
 */
fun TextView.makeSpannableSMSString(
    context: Context,
    content: String,
    startIndex: Int,
    lastIndex: Int,
    onClickListener: ClickableSpan? = null,
    isPrivacy: Boolean = false,
) {
    val myString = SpannableString(content)
//    val typeface = context.lightFont()
    myString.setSpan(
        onClickListener,
        startIndex,
        lastIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    myString.setSpan(
        StyleSpan(Typeface.NORMAL),
        startIndex,
        lastIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

//    if (isPrivacy) {
//        myString.setSpan(
//            ForegroundColorSpan(context.getColorCompat(R.color.rowTextColor)),
//            startIndex,
//            lastIndex,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//    } else {
//        myString.setSpan(
//            ForegroundColorSpan(context.getColorCompat(R.color.rowTextColor)),
//            startIndex,
//            lastIndex,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//    }

    this.text = myString
    this.highlightColor = Color.TRANSPARENT
    this.movementMethod = LinkMovementMethod.getInstance()


}

/**
 * method will use for get color using id
 * @param color - color id
 */

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

//fun Activity.findHostNavController(
//    @IdRes viewId: Int,
//    supportFragmentManager: FragmentManager
//): NavController {
//    return (supportFragmentManager
//        .findFragmentById(viewId) as NavHostFragment).findNavController()
//}


inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null) {
        ViewModelProvider(this).get(T::class.java)
    } else {
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
    }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null) {
        ViewModelProvider(this).get(T::class.java)
    } else {
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
    }
}

fun printThreadName() {
    Log.e("Thread", Thread.currentThread().name)
}

fun String?.nullSafe(): String {
    return if (this == null || this.isEmpty()) {
        ""
    } else {
        this
    }
}

fun Context.openWebPageUrlFromIntent(path: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(path)
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}


fun Context.openUrlFromIntent(mContext : Context,path: String) {
    try {
        val customIntent: CustomTabsIntent.Builder = CustomTabsIntent.Builder()

        customIntent.setToolbarColor(mContext.resources.getColor(R.color.colorPrimary))
        val packageName = "com.android.chrome"
            customIntent.build().intent.setPackage(packageName)
            customIntent.build().launchUrl(mContext, Uri.parse(path))
    } catch (e: ActivityNotFoundException) {
        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(path)))
    }
}

fun TextView.setCompoundDrawable(resId: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(
            this.context,
            resId
        ), null, null, null
    )
}

inline fun <reified T : Activity> Activity.startActivityInlineWithFinishAll(bundle: Bundle? = null) {
    if (bundle != null) {
        startActivity(
            Intent(this.applicationContext, T::class.java)
                .putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    } else {
        startActivity(
            Intent(
                this.applicationContext,
                T::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )

    }

    this.finishAffinity()
}

// Font
//fun Context.regularFont(): Typeface {
//    return ResourcesCompat.getFont(this, R.font.stc_forward_regular)!!
//}

fun Context.mediumFont(): Typeface {
    return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
}

fun Context.lightFont(): Typeface {
    return ResourcesCompat.getFont(this, R.font.stc_forward_light)!!
}

fun Context.boldFont(): Typeface {
    //  return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
    return ResourcesCompat.getFont(this, R.font.stc_forward_bold)!!
}


fun Context.chaletRegular(): Typeface {
    return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
}



/**
 * method will use for gone view
 */
fun View?.beGone() {
    if (this != null && this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}

/**
 * method will use for visible view
 */
fun View?.beVisible() {
    if (this != null && this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

/**
 * method will use for invisible view
 */
fun View?.beInVisible() {
    if (this != null && this.visibility != View.INVISIBLE) {
        this.visibility = View.INVISIBLE
    }
}
//
//fun getLastUpdateTime(time: Long): Pair<String, String> {
//    val calendar = Calendar.getInstance()
//    calendar.timeInMillis = time
//    val date = calendar.time
//    val simpleDateFormat = SimpleDateFormat(AppConstants.LAST_UPDATE_DATE_FORMAT, Locale.ITALIAN)
//    val strDate = simpleDateFormat.format(date)
//
//    val simpleDateFormatTime =
//        SimpleDateFormat(AppConstants.LAST_UPDATE_TIME_FORMAT, Locale.ITALIAN)
//    val strTime = simpleDateFormatTime.format(date)
//    return Pair(strDate, strTime)
//}
fun decimalFormat(context: Context, d2: Double): String? {

    val format = String.format("%.2f", d2)
    return Intrinsics.stringPlus("₹ ", format)
}

fun String.convertDateToRequireDateFormat(
    dateFormat: String,
    requiredFormat: String,
    locale: Locale = Locale.getDefault(),
): String {
    return try {
        val simpleDateFormat = SimpleDateFormat(dateFormat, locale)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val simpleDateFormatRequired = SimpleDateFormat(requiredFormat, locale)
        simpleDateFormatRequired.timeZone = TimeZone.getDefault()
        val secondDate = simpleDateFormat.parse(this)
        simpleDateFormatRequired.format(secondDate!!)
    } catch (e: Exception) {
        ""
    }
}

//fun FragmentManager.showNewFragment(fragment: Fragment) {
//    this.beginTransaction().replace(R.id.main_nav_host, fragment).commit()
//}
//
//fun FragmentManager.addNewFragment(key: String, fragment: Fragment, id: Int = R.id.main_nav_host) {
//    AppConstants.FRAGMENT_TAG = key
//    this.beginTransaction().add(id, fragment, AppConstants.FRAGMENT_TAG)
//        .addToBackStack(AppConstants.FRAGMENT_TAG)
//        .commitAllowingStateLoss()
//}
//
//
//// Remove Fragment From stack
//fun FragmentManager.removeAddedFragment(key: String) {
//    val fragment: Fragment? = this.findFragmentByTag(key)
//    fragment?.let {
//        this.beginTransaction().remove(it).commitNow()
//    }
//}
//
//fun FragmentManager.removeAllFragment() {
//    val count = this.backStackEntryCount
//    for (i in 0 until count) {
//        this.popBackStack()
//    }
//}


//fun Context.boldFont(): Typeface {
//    //  return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
//    return ResourcesCompat.getFont(this, R.font.roboto_bold)!!
//}
//
//fun Context.regularFont(): Typeface {
//    //  return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
//    return ResourcesCompat.getFont(this, R.font.roboto_regular)!!
//}
//
//fun Context.mediumFont(): Typeface {
//    //  return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
//    return ResourcesCompat.getFont(this, R.font.roboto_medium)!!
//}
//
//fun Context.italicFont(): Typeface {
//    //  return ResourcesCompat.getFont(this, R.font.stc_forward_medium)!!
//    return ResourcesCompat.getFont(this, R.font.roboto_italic)!!
//}

fun <T> getGsonString(t: T): String {
    return Gson().toJson(t)
}


fun String.convertDateToRequireDateLocalFormat(dateFormat: String, requiredFormat: String): String {
    return try {

        val simpleDateFormat = SimpleDateFormat(dateFormat)
        val simpleDateFormatRequired = SimpleDateFormat(requiredFormat)
        val secondDate = simpleDateFormat.parse(this)
        simpleDateFormatRequired.format(secondDate!!)
    } catch (e: Exception) {
        ""
    }
}

fun getE164PhoneNumber(phoneNumber: String): String {
    return try {
        return phoneNumber.substring(0..2)
            .plus(" ")
            .plus(phoneNumber.substring(3..5))
            .plus(" ")
            .plus(phoneNumber.substring(6..8))
            .plus(" ")
            .plus(phoneNumber.substring(9 until phoneNumber.length))

    } catch (e: Exception) {
        phoneNumber
    }
}

fun ImageView.loadImageFromServer(url: String?,placeHolderResId : Int? = 0) {
    Glide.with(this.context).asBitmap().load(url.nullSafe()).placeholder(placeHolderResId!!).into(this)
}
