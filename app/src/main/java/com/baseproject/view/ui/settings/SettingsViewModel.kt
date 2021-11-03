package com.baseproject.view.ui.settings

import com.baseproject.domain.enums.SocialStatus
import com.baseproject.view.base.BaseViewModel

class SettingsViewModel : BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>()  {

    override fun createInitialState(): SettingsContract.State {
        return SettingsContract.State.InitialState
    }

    override fun handleEvent(event: SettingsContract.Event) {
        when (event) {
            is SettingsContract.Event.OnSaveClicked -> {
                if (event.status == SocialStatus.BOGDAN)
                    setEffect { SettingsContract.Effect.ImpostorDialog }
                else
                    setEffect { SettingsContract.Effect.SaveSettings }
            }
            is SettingsContract.Event.OnBackClicked -> {
                setEffect { SettingsContract.Effect.NavigateUser }
            }
            is SettingsContract.Event.DialogDismiss -> {
                setEffect { SettingsContract.Effect.SaveSettings }
            }
        }
    }

}