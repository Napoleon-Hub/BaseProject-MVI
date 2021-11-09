package com.baseproject.view.ui.game

import android.content.res.Resources
import com.baseproject.domain.enums.Difficulty
import com.baseproject.view.base.UiEffect
import com.baseproject.view.base.UiEvent
import com.baseproject.view.base.UiState

class GameContract {

    sealed class Event : UiEvent {
        object GameStarted : Event()
        data class OnAutoLoseClicked(val score: Int, val resources: Resources) : Event()
        data class OnItemMissClick(val score: Int, val resources: Resources) : Event()
        data class GameFinished(val score: Int, val resources: Resources) : Event()
    }

    sealed class State : UiState {
        object PreparationGameState : State()
        object StartGameState : State()
    }

    sealed class Effect : UiEffect {
        object ShowLoseDialog : Effect()
        object ShowWinBottomSheetDialog : Effect()
        object ShowAchieveToast : Effect()
    }

}