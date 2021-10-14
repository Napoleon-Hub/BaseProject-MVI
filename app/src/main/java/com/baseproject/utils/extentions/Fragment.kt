package com.baseproject.utils.extentions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.toast(@StringRes messageStringRes: Int) {
    Toast.makeText(requireContext(), messageStringRes, Toast.LENGTH_SHORT).show()
}

fun Fragment.getNavResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavResult(result: String, key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}