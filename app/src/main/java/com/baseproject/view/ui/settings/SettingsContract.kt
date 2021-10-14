package com.baseproject.view.ui.settings

import com.baseproject.view.base.UiEffect
import com.baseproject.view.base.UiEvent
import com.baseproject.view.base.UiState

class SettingsContract {

    sealed class Event : UiEvent {
        object OnSaveClicked : Event()
        object OnBackClicked : Event()
    }

    sealed class State : UiState {
        object InitialState: State()
    }

    sealed class Effect : UiEffect {
        object SaveSettings : Effect()
        object NavigateUser : Effect()
    }

}