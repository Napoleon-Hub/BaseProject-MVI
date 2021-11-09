package com.baseproject.view.ui.achievements

import com.baseproject.view.base.UiEffect
import com.baseproject.view.base.UiEvent
import com.baseproject.view.base.UiState

class AchievementsContract {

    sealed class Event : UiEvent {
        object OnBackClicked : Event()
        object CheckBeCoolDialog : Event()
    }

    sealed class State : UiState {
        object InitialState: State()
    }

    sealed class Effect : UiEffect {
        object BeCoolDialog : Effect()
        object NavigateUser : Effect()
    }
}