package com.baseproject.view.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baseproject.R
import com.baseproject.data.prefs.PrefsEntity
import com.baseproject.databinding.FragmentSettingsBinding
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.utils.extentions.launchedWhenStarted
import com.baseproject.utils.extentions.setOnClickListener
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.game.dialog.GameDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings),
    DialogInterface.OnDismissListener {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var preferences: PrefsEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI() {
        initSpinner()
    }

    override fun initSetOnClickListeners() = binding.run {
        btnSave.setOnClickListener(500) {
            viewModel.setEvent(SettingsContract.Event.OnSaveClicked(idSocialStatusToEnum()))
        }
        btnBack.setOnClickListener(500) {
            viewModel.setEvent(SettingsContract.Event.OnBackClicked)
        }
    }

    // Way to process data with extensions
    override fun initObservers() {

        viewModel.uiState
            .onEach {
                when (it) {
                    SettingsContract.State.InitialState -> initMutableFields()
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
                        showImpostorDialog()
                    }
                }
            }
            .launchedWhenStarted(lifecycleScope)
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.settings_social_status_array,
            R.layout.item_spinner_style
        )
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)

        binding.spinner.adapter = adapter
    }

    private fun initMutableFields() = binding.run {
        etName.setText(preferences.name)
        spinner.setSelection(preferences.status.ordinal)
    }

    private fun saveSettingsFields() = preferences.run {
        name = binding.etName.text.toString()
        status = idSocialStatusToEnum()
    }

    private fun idSocialStatusToEnum(): Enum<SocialStatus> {
        return when (binding.spinner.selectedItemPosition) {
            0 -> SocialStatus.NAZI
            1 -> SocialStatus.PREGNANT
            2 -> SocialStatus.ALCOHOLIC
            3 -> SocialStatus.LUKASHENKA
            4 -> SocialStatus.KITTY
            5 -> SocialStatus.ON_PILLS
            else -> SocialStatus.BOGDAN
        }
    }

    private fun showImpostorDialog() {
        val description = getString(R.string.settings_impostor_dialog_description)
        GameDialogFragment(
            title = getString(R.string.settings_impostor_dialog_title),
            buttonText = getString(R.string.settings_impostor_dialog_button),
            status = description,
            listener = this@SettingsFragment
        ).show(childFragmentManager, null)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.setEvent(SettingsContract.Event.DialogDismiss)
    }


}