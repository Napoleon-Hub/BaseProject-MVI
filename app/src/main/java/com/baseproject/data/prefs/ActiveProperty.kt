package com.baseproject.data.prefs

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

abstract class ActiveProperty<T>(context: Context, private var key: String? = null) {

    protected val sp: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

    operator fun getValue(thisRef: PrefsEntity, property: KProperty<*>): T {
        return getFromPrefs(key ?: property.name)
    }

    operator fun setValue(thisRef: PrefsEntity, property: KProperty<*>, value: T) {
        saveToPrefs(key ?: property.name, value)
    }

    abstract fun getFromPrefs(key: String): T
    abstract fun saveToPrefs(key: String, value: T)


    companion object {
        const val PREFERENCES_KEY = "DefaultSharedPrefs"
    }
}



