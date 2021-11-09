package com.baseproject.view.ui.user

import com.baseproject.data.room.entity.AchievementsEntity
import com.baseproject.view.base.UiEffect
import com.baseproject.view.base.UiEvent
import com.baseproject.view.base.UiState

/**
 * Contract - это набор всех Event, State, Effect конкретной View
 */
class UserContract {

    /**
     * Events that user performed
     */
    sealed class Event : UiEvent {
        object OnSettingsClicked : Event()
        object OnStartClicked : Event()
        object OnAchievementsClicked : Event()
        object CheckAchievementsDatabase : Event()
        data class FillAchievementsDatabase(val list: List<AchievementsEntity>) : Event()
    }

    /**
     * Ui View States
     */
    sealed class State : UiState {
        object InitialState : State()
    }

    /**
     * Side effects
     */
    sealed class Effect : UiEffect {
        object FillAchievementsList : Effect()
        object NavigateSettings : Effect()
        object NavigateGame : Effect()
        object NavigateAchievements : Effect()
    }

}