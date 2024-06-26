package com.android.myapplication.utils

import android.content.Context
import android.util.AttributeSet
import com.android.myapplication.utils.extentions.lightFont
import com.android.myapplication.utils.extentions.mediumFont
import com.google.android.material.textfield.TextInputEditText

/**
 * @desc this class will handle TextInputEditText properties
 * @author : Priyanka Hirapara
 * @required
 
 **/
class CMEditTextView : TextInputEditText {

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
               // setTextColor(ContextCompat.getColor(context, R.color.white))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}