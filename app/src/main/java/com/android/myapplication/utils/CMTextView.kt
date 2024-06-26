package com.android.myapplication.utils

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import com.android.myapplication.utils.extentions.mediumFont

/**
 * @desc this class will handle TextView properties
 * @author : Priyanka Hirapara
 * @required
 
 **/
class CMTextView : AppCompatTextView {

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
                typeface = context.mediumFont()
                textDirection= TEXT_DIRECTION_LOCALE
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}