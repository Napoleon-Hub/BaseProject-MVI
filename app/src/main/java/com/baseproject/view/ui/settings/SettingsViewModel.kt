package com.baseproject.view.ui.settings

import androidx.lifecycle.viewModelScope
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.local.achievements.AchievementsRepository
import com.baseproject.utils.ACHIEVE_TRUTH_ID
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val achievementsRepository: AchievementsRepository,
    private val prefsEntity: PrefsEntity
) : BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>() {

    override fun createInitialState(): SettingsContract.State {
        return SettingsContract.State.InitialState
    }

    override fun handleEvent(event: SettingsContract.Event) {
        when (event) {
            is SettingsContract.Event.OnSaveClicked -> {
                when {
                    event.difficulty == Difficulty.TERMINATOR -> {
                        setEffect { SettingsContract.Effect.WarningDialog }
                    }
                    event.status == SocialStatus.BOGDAN -> {
                        setEffect { SettingsContract.Effect.ImpostorDialog }
                    }
                    else -> setEffect { SettingsContract.Effect.SaveSettings }
                }
            }
            is SettingsContract.Event.CheckTruth -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!achievementsRepository.getAchieveById(ACHIEVE_TRUTH_ID).achieveReceived) {
                        withContext(Dispatchers.Main) {
                            setEffect { SettingsContract.Effect.ShowAchieveToast }
                        }
                        achievementsRepository.updateReceipt(ACHIEVE_TRUTH_ID, true)
                    }
                }
            }
            is SettingsContract.Event.OnBackClicked -> {
                setEffect { SettingsContract.Effect.NavigateUser }
            }
            is SettingsContract.Event.DialogDismiss -> {
                setEffect { SettingsContract.Effect.SaveSettings }
            }
        }
    }

    var status: Enum<SocialStatus>
        get() = prefsEntity.status
        set(value) { prefsEntity.status = value }

    var difficulty: Enum<Difficulty>
        get() = prefsEntity.difficulty
        set(value) { prefsEntity.difficulty = value }

    var truth: Boolean
        get() = prefsEntity.truth
        set(value) { prefsEntity.truth = value }

}