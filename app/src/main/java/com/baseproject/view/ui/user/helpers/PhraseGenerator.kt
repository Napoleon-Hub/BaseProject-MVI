package com.baseproject.view.ui.user.helpers

import android.content.Context
import android.content.res.Resources
import com.baseproject.R
import com.baseproject.data.room.entity.BaseEntity

class PhraseGenerator {

    fun generateSharePhrase(baseEntity: BaseEntity, context: Context, resources: Resources): String {
        val phraseId = when (baseEntity.status) {
            0 -> R.string.generator_nazi
            1 -> R.string.generator_pregnant
            2 -> R.string.generator_alcoholic
            3 -> R.string.generator_lukashenka
            4 -> R.string.generator_kitty
            5 -> R.string.generator_on_pills
            6 -> R.string.generator_bogdan
            else -> R.string.generator_additional
        }
        val difficulty = resources.getStringArray(R.array.settings_difficulty_array)[baseEntity.difficulty]
        return context.getString(phraseId, difficulty, baseEntity.score)
    }

}