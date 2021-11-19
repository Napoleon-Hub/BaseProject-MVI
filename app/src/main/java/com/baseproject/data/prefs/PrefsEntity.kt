package com.baseproject.data.prefs

import android.content.Context
import com.baseproject.data.prefs.active_types.activeBoolean
import com.baseproject.data.prefs.active_types.activeEnum
import com.baseproject.data.prefs.active_types.activeInt
import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrefsEntity @Inject constructor(@ApplicationContext val ctx: Context) {

    var recordWeakling by activeInt(0)
    var recordTerminator by activeInt(0)
    var attempts by activeInt(0)
    var status by activeEnum(SocialStatus.ALCOHOLIC) { SocialStatus.valueOf(it) }
    var difficulty by activeEnum(Difficulty.WEAKLING) { Difficulty.valueOf(it) }
    var truth by activeBoolean(false)
    var isMuted by activeBoolean(false)
    val achievementsCount by activeInt(10)
    var achievementsReceived by activeInt(0)

    var firstBeCoolDialog by activeBoolean(true)
    var firstBogdanDialog by activeBoolean(true)
    var firstTerminatorDialog by activeBoolean(true)

}
