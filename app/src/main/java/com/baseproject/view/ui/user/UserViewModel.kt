package com.baseproject.view.ui.user

import com.baseproject.domain.local.entity.BaseEntityRepository
import com.baseproject.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val baseEntityRepository: BaseEntityRepository) :
    BaseViewModel<UserContract.Event, UserContract.State, UserContract.Effect>() {

    override fun createInitialState(): UserContract.State {
        return UserContract.State.InitialState
    }

    /**
     * This is a demonstration of the possibilities, in this case we can do without Effects,
     * but if we needed not only to go to another screen, then we would do just that.
     */
    override fun handleEvent(event: UserContract.Event) {
        when (event) {
            UserContract.Event.OnSettingsClicked -> {
                setEffect { UserContract.Effect.NavigateSettings }
            }
            UserContract.Event.OnStartClicked -> {
                setEffect { UserContract.Effect.NavigateGame }
            }
        }
    }

    fun getUserStatisticData() = baseEntityRepository.getAllEntities()

}