package com.baseproject.view.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baseproject.R
import com.baseproject.databinding.FragmentSettingsBinding
import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.utils.extentions.launchedWhenStarted
import com.baseproject.utils.extentions.setOnClickListener
import com.baseproject.utils.extentions.toast
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.game.dialog.GameDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings),
    DialogInterface.OnDismissListener {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    // Way to process data with extensions
    override fun initObservers() {

        viewModel.uiState
            .onEach {
                when (it) {
                    SettingsContract.State.InitialState -> {
                        initUI()
                        initSetOnClickListeners()
                    }
                }
            }
            .launchedWhenStarted(lifecycleScope)

        viewModel.effect
            .onEach {
                when (it) {
                    SettingsContract.Effect.SaveSettings -> {
                        saveSettingsFields()
                        navigateBack()
                    }
                    SettingsContract.Effect.NavigateUser -> {
                        navigateBack()
                    }
                    SettingsContract.Effect.ImpostorDialog -> {
                        showDialog(
                            titleId = R.string.settings_impostor_dialog_title,
                            buttonTextId = R.string.settings_impostor_dialog_button,
                            descriptionId = R.string.settings_impostor_dialog_description
                        )
                    }
                    SettingsContract.Effect.WarningDialog -> {
                        showDialog(
                            titleId = R.string.settings_warning_dialog_title,
                            buttonTextId = R.string.settings_warning_dialog_button,
                            descriptionId = R.string.settings_warning_dialog_description
                        )
                    }
                    SettingsContract.Effect.ShowAchieveToast -> {
                        toast(R.string.achievements_toast_text)
                    }
                }
            }
            .launchedWhenStarted(lifecycleScope)
    }

    override fun initUI() {
        initSpinners(
            mapOf(
                Pair(R.array.settings_social_status_array, binding.spinnerStatus),
                Pair(R.array.settings_difficulty_array, binding.spinnerDifficulty)
            )
        )
        initMutableFields()
    }

    override fun initSetOnClickListeners() = binding.run {
        btnSave.setOnClickListener(500) {
            if (binding.rbTruth.isChecked) viewModel.setEvent(SettingsContract.Event.CheckTruth)
            viewModel.setEvent(SettingsContract.Event.OnSaveClicked(idSocialStatusToEnum(), idDifficultyToEnum()))
        }
        btnBack.setOnClickListener(500) {
            viewModel.setEvent(SettingsContract.Event.OnBackClicked)
        }
    }

    private fun initSpinners(map: Map<Int, Spinner>) {
        map.forEach {
            val textArrayResId = it.key
            val spinner = it.value

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                textArrayResId,
                R.layout.item_spinner_style
            )
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner.adapter = adapter
        }
    }

    private fun initMutableFields() = binding.run {
        spinnerStatus.setSelection(viewModel.status.ordinal)
        spinnerDifficulty.setSelection(viewModel.difficulty.ordinal)
        rbTruth.isChecked = viewModel.truth
    }

    private fun saveSettingsFields() = viewModel.run {
        status = idSocialStatusToEnum()
        difficulty = idDifficultyToEnum()
        truth = binding.rbTruth.isChecked
    }

    private fun idSocialStatusToEnum(): Enum<SocialStatus> {
        return when (binding.spinnerStatus.selectedItemPosition) {
            0 -> SocialStatus.NAZI
            1 -> SocialStatus.PREGNANT
            2 -> SocialStatus.ALCOHOLIC
            3 -> SocialStatus.LUKASHENKA
            4 -> SocialStatus.KITTY
            5 -> SocialStatus.ON_PILLS
            else -> SocialStatus.BOGDAN
        }
    }

    private fun idDifficultyToEnum(): Enum<Difficulty> {
        return when (binding.spinnerDifficulty.selectedItemPosition) {
            0 -> Difficulty.WEAKLING
            else -> Difficulty.TERMINATOR
        }
    }

    private fun showDialog(titleId: Int, buttonTextId: Int, descriptionId: Int) {
        GameDialogFragment(
            title = getString(titleId),
            buttonText = getString(buttonTextId),
            status = getString(descriptionId),
            listener = this@SettingsFragment
        ).show(childFragmentManager, null)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.setEvent(SettingsContract.Event.DialogDismiss)
    }

}