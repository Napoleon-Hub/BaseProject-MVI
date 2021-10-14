package com.baseproject.data.prefs

import android.content.Context
import com.baseproject.data.prefs.active_types.activeEnum
import com.baseproject.data.prefs.active_types.activeInt
import com.baseproject.data.prefs.active_types.activeString
import com.baseproject.domain.enums.SocialStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefsEntity @Inject constructor(@ApplicationContext val ctx: Context) {

    var level by activeInt(0)
    var attempts by activeInt(0)
    var name by activeString("Hullabaloo")
    var status by activeEnum(SocialStatus.ALCOHOLIC) { SocialStatus.valueOf(it) }

}
