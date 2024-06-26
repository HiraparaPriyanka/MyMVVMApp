package com.android.myapplication.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.android.myapplication.utils.extentions.lightFont

/**
 * @desc this class will handle TextView properties
 * @author : Priyanka Hirapara
 * @required

 **/
class CRTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    fun init() {
        if (!isInEditMode) {
            try {
                typeface = context.lightFont()
                textDirection= TEXT_DIRECTION_LOCALE
               // setTextColor(ContextCompat.getColor(context, R.color.white))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}