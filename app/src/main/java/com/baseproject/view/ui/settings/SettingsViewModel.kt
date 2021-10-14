package com.baseproject.view.ui.settings

import com.baseproject.view.base.BaseViewModel

class SettingsViewModel : BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>()  {

    override fun createInitialState(): SettingsContract.State {
        return SettingsContract.State.InitialState
    }

    override fun handleEvent(event: SettingsContract.Event) {
        when (event) {
            SettingsContract.Event.OnSaveClicked -> {
                setEffect { SettingsContract.Effect.SaveSettings }
            }
            SettingsContract.Event.OnBackClicked -> {
                setEffect { SettingsContract.Effect.NavigateUser }
            }
        }
    }

}