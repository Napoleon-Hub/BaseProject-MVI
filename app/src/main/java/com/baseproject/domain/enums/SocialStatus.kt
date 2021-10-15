package com.baseproject.domain.enums

import java.util.*

enum class SocialStatus {
    NAZI,
    PREGNANT,
    ALCOHOLIC,
    LUKASHENKA,
    KITTY,
    ON_PILLS;

    override fun toString(): String {
        return name[0].toString() + name.substring(1).lowercase(Locale.getDefault())
    }
}