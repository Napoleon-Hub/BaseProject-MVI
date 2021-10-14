package com.baseproject.data.prefs.active_types

import android.content.Context
import com.baseproject.data.prefs.ActiveProperty
import com.baseproject.data.prefs.PrefsEntity

class ActiveLong(context: Context) : ActiveProperty<Long>(context) {
    override fun getFromPrefs(key: String): Long {
        return sp.getLong(key, 0L)
    }

    override fun saveToPrefs(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }
}

fun PrefsEntity.activeLong() = ActiveLong(ctx)