package com.baseproject.view.ui.user

import androidx.lifecycle.viewModelScope
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.local.achievements.AchievementsRepository
import com.baseproject.domain.local.entity.BaseEntityRepository
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val baseEntityRepository: BaseEntityRepository,
    private val achievementsRepository: AchievementsRepository,
    private val prefsEntity: PrefsEntity
) : BaseViewModel<UserContract.Event, UserContract.State, UserContract.Effect>() {

    override fun createInitialState(): UserContract.State {
        return UserContract.State.InitialState
    }

    /**
     * This is a demonstration of the possibilities, in this case we can do without Effects,
     * but if we needed not only to go to another screen, then we would do just that.
     */
    override fun handleEvent(event: UserContract.Event) {
        when (event) {
            UserContract.Event.CheckAchievementsDatabase -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val isAchievementsDatabaseFilled =
                        prefsEntity.achievementsCount == achievementsRepository.getEntitiesCount()
                    if (!isAchievementsDatabaseFilled) setEffect { UserContract.Effect.FillAchievementsList }
                }
            }
            is UserContract.Event.FillAchievementsDatabase -> {
                viewModelScope.launch {
                    achievementsRepository.insertAchievements(event.list)
                }
            }
            UserContract.Event.OnSettingsClicked -> {
                setEffect { UserContract.Effect.NavigateSettings }
            }
            UserContract.Event.OnStartClicked -> {
                setEffect { UserContract.Effect.NavigateGame }
            }
            UserContract.Event.OnAchievementsClicked -> {
                setEffect { UserContract.Effect.NavigateAchievements }
            }
        }
    }

    fun getUserStatisticData() = baseEntityRepository.getAllEntities()

    val recordWeakling: Int
        get() = prefsEntity.recordWeakling

    val recordTerminator: Int
        get() = prefsEntity.recordTerminator

    val attempts: Int
        get() = prefsEntity.attempts

    val status: Enum<SocialStatus>
        get() = prefsEntity.status

}