package com.baseproject.view.ui.achievements

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baseproject.R
import com.baseproject.databinding.FragmentAchievementsBinding
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.achievements.adapter.AchievementsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.baseproject.utils.extentions.setOnClickListener
import com.baseproject.view.ui.game.dialog.GameDialogFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AchievementsFragment : BaseFragment(R.layout.fragment_achievements), DialogInterface.OnDismissListener  {

    private lateinit var binding: FragmentAchievementsBinding

    private val viewModel: AchievementsViewModel by viewModels()

    private val adapter by lazy { AchievementsRecyclerViewAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAchievementsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setEvent(AchievementsContract.Event.CheckBeCoolDialog)
    }

    override fun initObservers() {

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                when (it) {
                    AchievementsContract.State.InitialState -> {
                        initUI()
                        initSetOnClickListeners()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    AchievementsContract.Effect.BeCoolDialog -> {
                        showDialog(
                            R.string.achievements_dialog_be_cool_title,
                            R.string.achievements_dialog_be_cool_button,
                            R.string.achievements_dialog_be_cool_description
                        )
                    }
                    AchievementsContract.Effect.NavigateUser -> navigateBack()
                }
            }
        }

        viewModel.achievements.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }


    override fun initUI() = binding.run {
        rvAchievements.adapter = adapter
    }

    override fun initSetOnClickListeners() = binding.run {
        btnBack.setOnClickListener(500) {
            viewModel.setEvent(AchievementsContract.Event.OnBackClicked)
        }
    }

    private fun showDialog(titleId: Int, buttonTextId: Int, descriptionId: Int) {
        GameDialogFragment(
            title = getString(titleId),
            buttonText = getString(buttonTextId),
            status = getString(descriptionId),
            listener = this@AchievementsFragment
        ).show(childFragmentManager, null)
    }

    override fun onDismiss(dialog: DialogInterface?) {}

}