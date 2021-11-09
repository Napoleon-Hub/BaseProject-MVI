package com.baseproject.domain.enums

import android.content.res.Resources
import com.baseproject.R

enum class SocialStatus {
    NAZI,
    PREGNANT,
    ALCOHOLIC,
    LUKASHENKA,
    KITTY,
    ON_PILLS,
    BOGDAN
}

fun Enum<SocialStatus>.getStatusName(resources: Resources): String {
    val socialStatusArray = resources.getStringArray(R.array.settings_social_status_array)
    return when(this) {
        SocialStatus.NAZI       -> socialStatusArray[0]
        SocialStatus.PREGNANT   -> socialStatusArray[1]
        SocialStatus.ALCOHOLIC  -> socialStatusArray[2]
        SocialStatus.LUKASHENKA -> socialStatusArray[3]
        SocialStatus.KITTY      -> socialStatusArray[4]
        SocialStatus.ON_PILLS   -> socialStatusArray[5]
        else                    -> socialStatusArray[6]
    }
}