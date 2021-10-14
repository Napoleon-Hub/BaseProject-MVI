package com.baseproject.data.prefs.active_types

import android.content.Context
import com.baseproject.data.prefs.ActiveProperty
import com.baseproject.data.prefs.PrefsEntity

class ActiveInt(ctx: Context, private val defValue: Int = 0) : ActiveProperty<Int>(ctx) {
    override fun getFromPrefs(key: String): Int {
        return sp.getInt(key, defValue)
    }

    override fun saveToPrefs(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }
}

fun PrefsEntity.activeInt(defValue: Int = 0) = ActiveInt(ctx, defValue)