package com.baseproject.data.prefs.active_types

import android.content.Context
import com.baseproject.data.prefs.ActiveProperty
import com.baseproject.data.prefs.PrefsEntity

/*******************************************
 *
 * Usage: call activeEnum() with two arguments:
 *  - default enum value: YourEnum.YOUR_DEFAULT
 *  - {YourEnum.valueOf(it)}
 *  This lambda is needed to cast string
 *  from shared preferences to actual enum
 *
 ********************************************/

class ActiveEnum<T : Enum<T>>(
    context: Context,
    private val getEnumByName: (String) -> Enum<T>,
    private val defValue: Enum<T>,
    key: String? = null
) : ActiveProperty<Enum<T>>(context, key) {

    override fun getFromPrefs(key: String): Enum<T> {
        val name = sp.getString(key, null)
        name?.let { return getEnumByName(it) }
        return defValue
    }

    override fun saveToPrefs(key: String, value: Enum<T>) {
        sp.edit().putString(key, value.name).apply()
    }
}

fun <T : Enum<T>> PrefsEntity.activeEnum(
    defValue: Enum<T>,
    key: String? = null,
    getEnumByName: (String) -> Enum<T>
): ActiveEnum<T> {
    return ActiveEnum(ctx, getEnumByName, defValue, key)
}