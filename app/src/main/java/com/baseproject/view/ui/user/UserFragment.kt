package com.baseproject.view.ui.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baseproject.R
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.databinding.FragmentUserBinding
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.enums.SocialStatus.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.baseproject.utils.extentions.setOnClickListener
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.user.adapter.UserRecyclerViewAdapter
import javax.inject.Inject


@AndroidEntryPoint
class UserFragment : BaseFragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding

    private val viewModel: UserViewModel by viewModels()

    @Inject
    lateinit var preferences: PrefsEntity

    private val adapter by lazy { UserRecyclerViewAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentUserBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI() = binding.run {
        rvGamesStatistics.adapter = adapter
    }

    override fun initSetOnClickListeners() = binding.run {
        btnStart.setOnClickListener(500) {
            viewModel.setEvent(UserContract.Event.OnStartClicked)
        }
        btnSettings.setOnClickListener(500) {
            viewModel.setEvent(UserContract.Event.OnSettingsClicked)
        }
    }

    override fun initObservers() {

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                when (it) {
                    UserContract.State.InitialState -> initMutableFields()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    UserContract.Effect.NavigateSettings -> {
                        navigate(UserFragmentDirections.actionUserFragmentToSettingsFragment())
                    }
                    UserContract.Effect.NavigateGame -> {
                        navigate(UserFragmentDirections.actionUserFragmentToGameFragment())
                    }
                }
            }
        }

        viewModel.getUserStatisticData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            (binding.rvGamesStatistics.layoutManager as LinearLayoutManager).reverseLayout = true
        }

    }

    private fun initMutableFields() = binding.apply {
        tvWins.text = getString(R.string.user_wins, preferences.wins)
        tvAttempts.text = getString(R.string.user_attempts, preferences.attempts)
        tvName.text = getString(R.string.user_name, preferences.name)
        tvStatus.text = getString(R.string.user_social_status, preferences.status.getStatusName())
    }

    private fun Enum<SocialStatus>.getStatusName(): String {
        val socialStatusArray = resources.getStringArray(R.array.settings_social_status_array)
        return when(this) {
            NAZI       -> socialStatusArray[0]
            PREGNANT   -> socialStatusArray[1]
            ALCOHOLIC  -> socialStatusArray[2]
            LUKASHENKA -> socialStatusArray[3]
            KITTY      -> socialStatusArray[4]
            ON_PILLS   -> socialStatusArray[5]
            else       -> socialStatusArray[6]
        }
    }

}