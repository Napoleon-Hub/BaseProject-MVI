package com.baseproject.view.ui.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baseproject.R
import com.baseproject.data.room.entity.AchievementsEntity
import com.baseproject.databinding.FragmentUserBinding
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.domain.enums.getStatusName
import com.baseproject.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.baseproject.utils.extentions.setOnClickListener
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.user.adapter.UserRecyclerViewAdapter


@AndroidEntryPoint
class UserFragment : BaseFragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding

    private val viewModel: UserViewModel by viewModels()

    private val adapter by lazy { UserRecyclerViewAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentUserBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setEvent(UserContract.Event.CheckAchievementsDatabase)
    }

    override fun initObservers() {

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                when (it) {
                    UserContract.State.InitialState -> {
                        initUI()
                        initSetOnClickListeners()
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                when (it) {
                    UserContract.Effect.FillAchievementsList -> {
                        val achievementsList = createAchievementsList()
                        viewModel.setEvent(UserContract.Event.FillAchievementsDatabase(achievementsList))
                    }
                    UserContract.Effect.NavigateSettings -> {
                        navigate(UserFragmentDirections.actionUserFragmentToSettingsFragment())
                    }
                    UserContract.Effect.NavigateGame -> {
                        navigate(UserFragmentDirections.actionUserFragmentToGameFragment())
                    }
                    UserContract.Effect.NavigateAchievements -> {
                        navigate(UserFragmentDirections.actionUserFragmentToAchievementsFragment())
                    }
                }
            }
        }

        viewModel.getUserStatisticData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            (binding.rvGamesStatistics.layoutManager as LinearLayoutManager).reverseLayout = true
        }

    }

    override fun initUI() {
        binding.rvGamesStatistics.adapter = adapter
        initMutableFields()
    }

    override fun initSetOnClickListeners() = binding.run {
        btnStart.setOnClickListener(500) {
            viewModel.setEvent(UserContract.Event.OnStartClicked)
        }
        btnSettings.setOnClickListener(500) {
            viewModel.setEvent(UserContract.Event.OnSettingsClicked)
        }
        btnAchievements.setOnClickListener(500) {
            viewModel.setEvent(UserContract.Event.OnAchievementsClicked)
        }
    }

    private fun initMutableFields() = binding.apply {
        tvRecord.text = getString(R.string.user_record, viewModel.getRecord())
        tvAttempts.text = getString(R.string.user_attempts, viewModel.getAttempts())
        tvStatus.text = getString(R.string.user_social_status, viewModel.getStatus())
    }


    private fun createAchievementsList(): List<AchievementsEntity> = listOf(
        AchievementsEntity(
            SocialStatus.NAZI.getStatusName(resources), R.drawable.img_nazi,
            R.string.achievements_nazi_title, R.string.achievements_nazi_description
        ),
        AchievementsEntity(
            SocialStatus.PREGNANT.getStatusName(resources), R.drawable.img_pregnant,
            R.string.achievements_pregnant_title, R.string.achievements_pregnant_description
        ),
        AchievementsEntity(
            SocialStatus.ALCOHOLIC.getStatusName(resources), R.drawable.img_alcoholic,
            R.string.achievements_alcoholic_title, R.string.achievements_alcoholic_description
        ),
        AchievementsEntity(
            SocialStatus.LUKASHENKA.getStatusName(resources), R.drawable.img_lukashenka,
            R.string.achievements_lukashenka_title, R.string.achievements_lukashenka_description
        ),
        AchievementsEntity(
            SocialStatus.KITTY.getStatusName(resources), R.drawable.img_kitty,
            R.string.achievements_kitty_title, R.string.achievements_kitty_description
        ),
        AchievementsEntity(
            SocialStatus.ON_PILLS.getStatusName(resources), R.drawable.img_on_pills,
            R.string.achievements_on_pills_title, R.string.achievements_on_pills_description
        ),
        AchievementsEntity(
            SocialStatus.BOGDAN.getStatusName(resources), R.drawable.img_bogdan,
            R.string.achievements_bogdan_title, R.string.achievements_bogdan_description
        ),
        AchievementsEntity(
            ACHIEVE_LOSER_ID, R.drawable.img_loser,
            R.string.achievements_loser_title, R.string.achievements_loser_description
        ),
        AchievementsEntity(
            ACHIEVE_TRUTH_ID, R.drawable.img_truth,
            R.string.achievements_truth_title, R.string.achievements_truth_description
        )
    )
}