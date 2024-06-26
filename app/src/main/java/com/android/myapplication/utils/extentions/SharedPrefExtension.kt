package com.android.myapplication.utils.extentions

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import java.util.Date


inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

internal var SharedPreferences.lastUpdateTime: Long
    set(value) {
        edit { putLong("lastUpdateTime", value) }
    }
    get() {
        getLong("lastUpdateTime", Date().time).let {
            return it
        }
    }

internal var SharedPreferences.isLoggedIn: Boolean
    set(value) {
        edit { putBoolean("is_loggedIn", value) }
    }
    get() {
        getBoolean("is_loggedIn", false).let {
            return it
        }
    }

internal var SharedPreferences.userID: String?
    set(value) {
        edit { putString("user_id", value) }
    }
    get() {
        getString("user_id", "").let {
            return it
        }
    }

internal var SharedPreferences.sendSMS: Boolean
    set(value) {
        edit { putBoolean("send_sms", value) }
    }
    get() {
        getBoolean("send_sms", false).let {
            return it
        }
    }

internal var SharedPreferences.number: String?
    set(value) {
        edit { putString("user_number", value) }
    }
    get() {
        getString("user_number", "").let {
            return it
        }
    }

internal var SharedPreferences.isRate: Boolean
    set(value) {
        edit { putBoolean("is_Rate", value) }
    }
    get() {
        getBoolean("is_Rate", false).let {
            return it
        }
    }

internal var SharedPreferences.onBoardComplete: Boolean
    set(value) {
        edit { putBoolean("is_onBoard", value) }
    }
    get() {
        getBoolean("is_onBoard", false).let {
            return it
        }
    }


internal var SharedPreferences.accessToken: String
    set(value) {
        edit { putString("access_token", value) }
    }
    get() {
        getString("access_token", "").let {
            return it.toString()
        }
    }

fun <T> SharedPreferences.set(key: String, `object`: T) {

    `object` ?: return
    val editor = this.edit()
    //Convert object to JSON String.
    val jsonString = GsonBuilder().create().toJson(`object`)
    //Save that String in SharedPreferences
    editor.putString(key, jsonString).apply()
}


inline fun <reified T> SharedPreferences.get(key: String): T? {
    //We read JSON String which was saved.
    val value = this.getString(key, null)
    value ?: return null
    //JSON String was found which means object can be read.
    //We convert this JSON String to model object. Parameter "c" (of
    //type Class < T >" is used to cast.
    return GsonBuilder().create().fromJson(value, T::class.java)
}

fun SharedPreferences.clear() {
    val editor = this.edit()
    editor?.clear()
    editor?.apply()
    editor?.commit()
}
