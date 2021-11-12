package com.baseproject.view.ui.game

import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.data.room.entity.BaseEntity
import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.enums.getStatusName
import com.baseproject.domain.local.achievements.AchievementsRepository
import com.baseproject.domain.local.entity.BaseEntityRepository
import com.baseproject.utils.ACHIEVE_LOSER_ID
import com.baseproject.utils.ACHIEVE_WEAKLING_ID
import com.baseproject.utils.WIN_TERMINATOR_SCORE
import com.baseproject.utils.WIN_WEAKLING_SCORE
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val baseEntityRepository: BaseEntityRepository,
    private val achievementsRepository: AchievementsRepository,
    private val prefsEntity: PrefsEntity
) : BaseViewModel<GameContract.Event, GameContract.State, GameContract.Effect>() {

    override fun createInitialState(): GameContract.State {
        return GameContract.State.PreparationGameState
    }

    override fun handleEvent(event: GameContract.Event) {
        when (event) {
            GameContract.Event.GameStarted -> setState { GameContract.State.StartGameState }
            is GameContract.Event.OnAutoLoseClicked -> endGame(event.score, event.resources)
            is GameContract.Event.OnItemMissClick -> endGame(event.score, event.resources)
            is GameContract.Event.GameFinished -> endGame(event.score, event.resources)
        }
    }

    private fun endGame(score: Int, resources: Resources) {
        val neededScores = if (difficulty == Difficulty.TERMINATOR) WIN_TERMINATOR_SCORE else WIN_WEAKLING_SCORE
        val isWin = score >= neededScores
        if (isWin) {
            setEffect { GameContract.Effect.ShowWinBottomSheetDialog }
            if (difficulty == Difficulty.TERMINATOR) {
                viewModelScope.launch(Dispatchers.IO) {
                    val achieveId = status.getStatusName(resources)
                    if (!achievementsRepository.getAchieveById(achieveId).achieveReceived) {
                        getAchieve(achieveId)
                    }
                }
            } else if (difficulty == Difficulty.WEAKLING) {
                viewModelScope.launch(Dispatchers.IO) {
                    if (!achievementsRepository.getAchieveById(ACHIEVE_WEAKLING_ID).achieveReceived) {
                        getAchieve(ACHIEVE_WEAKLING_ID)
                    }
                }
            }
        } else {
            setEffect { GameContract.Effect.ShowLoseDialog }
            viewModelScope.launch(Dispatchers.IO) {
                if (score < 10 && !achievementsRepository.getAchieveById(ACHIEVE_LOSER_ID).achieveReceived) {
                    getAchieve(ACHIEVE_LOSER_ID)
                }
            }
        }
        saveResult(score, isWin)
        updateUserExperience(score)
    }

    private suspend fun getAchieve(id: String) {
        setEffect { GameContract.Effect.ShowAchieveToast }
        achievementsReceived++
        achievementsRepository.updateReceipt(id, true)
    }

    private fun saveResult(score: Int, isWin: Boolean) {
        viewModelScope.launch {
            val count = baseEntityRepository.getEntitiesCount()
            baseEntityRepository.insertEntity(
                BaseEntity(
                    id = count, score = score, isWin = isWin,
                    difficulty = difficulty.ordinal, status = status.ordinal
                )
            )
        }
    }

    private fun updateUserExperience(score: Int) {
        if (score > record) record = score
        attempts++
    }

    val status: Enum<SocialStatus>
        get() = prefsEntity.status

    val difficulty: Enum<Difficulty>
        get() = prefsEntity.difficulty

    private var attempts: Int
        get() = prefsEntity.attempts
        set(value) { prefsEntity.attempts = value }

    private var record: Int
        get() = prefsEntity.record
        set(value) { prefsEntity.record = value }

    private var achievementsReceived: Int
        get() = prefsEntity.achievementsReceived
        set(value) { prefsEntity.achievementsReceived = value }

}