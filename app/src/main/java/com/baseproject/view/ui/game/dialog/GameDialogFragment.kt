package com.baseproject.view.ui.game.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.baseproject.R
import com.baseproject.databinding.DialogLoseBinding
import com.baseproject.utils.extentions.setOnClickListener

class GameDialogFragment(
    private val title: String? = null,
    private val buttonText: String? = null,
    private val status: String,
    listener: DialogInterface.OnDismissListener
) : DialogFragment() {

    private lateinit var binding: DialogLoseBinding

    private var buttonListener: DialogInterface.OnDismissListener = listener

    override fun getTheme() = R.style.DialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLoseBinding.bind(inflater.inflate(R.layout.dialog_lose, container))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() = binding.run {
        if (title != null) tvDialogTitle.text = title
        if (buttonText != null) btnCancelLoseDialog.text = buttonText
        tvDialogDescription.text = status
        btnCancelLoseDialog.setOnClickListener(500) { dialog?.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        buttonListener.onDismiss(dialog)
    }

}