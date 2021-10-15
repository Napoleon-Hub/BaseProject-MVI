package com.baseproject.view.ui.game.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baseproject.R
import com.baseproject.databinding.DialogBottomSheetWinBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.baseproject.utils.extentions.setOnClickListener

class BottomSheetWinFragment(
    private val status: String,
    listener: DialogInterface.OnDismissListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetWinBinding

    private var buttonListener: DialogInterface.OnDismissListener = listener

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetWinBinding.bind(
            inflater.inflate(R.layout.dialog_bottom_sheet_win, container)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() = binding.run {
        tvWinDescription.text = status
        btnCancel.setOnClickListener(500) { dialog?.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        buttonListener.onDismiss(dialog)
    }

}