package com.baseproject.view.ui.settings

import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.view.base.UiEffect
import com.baseproject.view.base.UiEvent
import com.baseproject.view.base.UiState

class SettingsContract {

    sealed class Event : UiEvent {
        class OnSaveClicked(val status: Enum<SocialStatus>, val difficulty: Enum<Difficulty>) : Event()
        object CheckTruth : Event()
        object OnBackClicked : Event()
        object DialogDismiss : Event()
    }

    sealed class State : UiState {
        object InitialState : State()
    }

    sealed class Effect : UiEffect {
        object SaveSettings : Effect()
        object NavigateUser : Effect()
        object ImpostorDialog : Effect()
        object WarningDialog : Effect()
    }

}