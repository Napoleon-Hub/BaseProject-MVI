package com.baseproject.view.ui.achievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.data.room.entity.AchievementsEntity
import com.baseproject.domain.local.achievements.AchievementsRepository
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    achievementsRepository: AchievementsRepository,
    private val prefsEntity: PrefsEntity
) : BaseViewModel<AchievementsContract.Event, AchievementsContract.State, AchievementsContract.Effect>() {

    val achievements: LiveData<List<AchievementsEntity>> = achievementsRepository.getAllAchievements().asLiveData()

    override fun createInitialState(): AchievementsContract.State {
        return AchievementsContract.State.InitialState
    }

    override fun handleEvent(event: AchievementsContract.Event) {
        when (event) {
            is AchievementsContract.Event.OnBackClicked ->
                setEffect { AchievementsContract.Effect.NavigateUser }
            is AchievementsContract.Event.CheckBeCoolDialog -> {
                if (firstBeCoolDialog) {
                    firstBeCoolDialog = false
                    setEffect { AchievementsContract.Effect.BeCoolDialog }
                }
            }
        }
    }

    private var firstBeCoolDialog: Boolean
        get() = prefsEntity.firstBeCoolDialog
        set(value) { prefsEntity.firstBeCoolDialog = value }

}