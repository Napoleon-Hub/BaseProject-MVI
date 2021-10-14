package com.baseproject.data.prefs.active_types

import android.content.Context
import com.baseproject.data.prefs.ActiveProperty
import com.baseproject.data.prefs.PrefsEntity

class ActiveFloat(context: Context) : ActiveProperty<Float>(context) {
    override fun getFromPrefs(key: String): Float {
        return sp.getFloat(key, 0F)
    }

    override fun saveToPrefs(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }
}

fun PrefsEntity.activeFloat() = ActiveFloat(ctx)