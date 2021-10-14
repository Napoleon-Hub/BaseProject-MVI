package com.baseproject.utils.validation

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(RegExp.NICKNAME, RegExp.EMAIL)
annotation class RegExp {
    companion object {
        const val EMAIL =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" + ")+"

        const val NICKNAME = "[a-z]{0,}" + "[a-z1-5]{0,11}"
        const val PHONE = "[^0-9]"
    }
}
