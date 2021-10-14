package com.baseproject.view.ui.game.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.baseproject.R
import com.baseproject.databinding.DialogResultGameBinding

class ResultGameDialogFragment : DialogFragment() {

    private lateinit var binding: DialogResultGameBinding

    // In this case we didn't use it
    var positiveBtnListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DialogResultGameBinding.bind(inflater.inflate(R.layout.dialog_result_game, container))
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelable<ResultGameDialogData>(DATA_KEY)
        if (data != null) setupDialogContent(data)
    }


    private fun setupDialogContent(data: ResultGameDialogData) {
        with(binding) {
            tvDialogTitle.text = data.title
            if (data.description != null) {
                tvDialogDescription.text = data.description
            } else tvDialogDescription.visibility = View.GONE
            mbPositive.setupButton(data.positiveBtnText, positiveBtnListener)
        }

    }

    private fun TextView.setupButton(btnText: String?, listener: (() -> Unit)?) {
        apply {
            if (btnText != null) {
                text = btnText
                setOnClickListener {
                    dismiss()
                    listener?.invoke()
                }
            } else {
                isVisible = false
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (parentFragment is DialogInterface.OnDismissListener)
            (parentFragment as DialogInterface.OnDismissListener).onDismiss(dialog)
    }

    companion object {
        fun newInstance(data: ResultGameDialogData) = ResultGameDialogFragment().apply {
            arguments = Bundle().apply { putParcelable(DATA_KEY, data) }
        }

        private const val DATA_KEY = "fe7ref8ef"
    }
}