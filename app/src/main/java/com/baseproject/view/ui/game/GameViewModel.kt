package com.baseproject.view.ui.game

import androidx.lifecycle.viewModelScope
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.data.room.entity.BaseEntity
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.local.entity.BaseEntityRepository
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val baseEntityRepository: BaseEntityRepository,
    private val prefsEntity: PrefsEntity
) : BaseViewModel<GameContract.Event, GameContract.State, GameContract.Effect>() {

    override fun createInitialState(): GameContract.State {
        return GameContract.State.PreparationGameState
    }

    override fun handleEvent(event: GameContract.Event) {
        when (event) {
            is GameContract.Event.GameStarted -> setState { GameContract.State.StartGameState }
            is GameContract.Event.OnAutoLoseClicked -> endGame(event.score)
            is GameContract.Event.OnItemMissClick -> endGame(event.score)
            is GameContract.Event.GameFinished -> endGame(event.score)
        }
    }

    private fun endGame(score: Int) {
        val isWin = score >= WIN_SCORE
        if (isWin) setEffect { GameContract.Effect.ShowWinBottomSheetDialog }
        else setEffect { GameContract.Effect.ShowLoseDialog }
        saveResult(score, isWin)
        updateUserExperience(isWin)
    }

    private fun saveResult(score: Int, isWin: Boolean) {
        viewModelScope.launch {
            val count = baseEntityRepository.getEntitiesCount()
            baseEntityRepository.insertEntity(BaseEntity(id = count, score = score, isWin = isWin))
        }
    }

    private fun updateUserExperience(isWin: Boolean) = prefsEntity.apply {
        if (isWin) wins++
        attempts++
    }

    fun getUserStatus(): Enum<SocialStatus> = prefsEntity.status

    companion object {
        const val WIN_SCORE = 150
    }

}