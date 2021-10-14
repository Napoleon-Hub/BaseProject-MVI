package com.baseproject.data.prefs.active_types

import android.content.Context
import com.baseproject.data.prefs.ActiveProperty
import com.baseproject.data.prefs.PrefsEntity

class ActiveString(context: Context, private val defValue: String = "") :
    ActiveProperty<String>(context) {
    override fun getFromPrefs(key: String): String {
        return sp.getString(key, defValue) ?: ""
    }

    override fun saveToPrefs(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }
}

fun PrefsEntity.activeString(defValue: String = "") = ActiveString(ctx, defValue)