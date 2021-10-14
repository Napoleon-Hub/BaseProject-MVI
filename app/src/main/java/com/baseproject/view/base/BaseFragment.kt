package com.baseproject.view.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initSetOnClickListeners()
        initObservers()
    }

    /**
     * If you need initialize any UI by default do it in initUI()
     */
    abstract fun initUI()
    abstract fun initSetOnClickListeners()
    abstract fun initObservers()

    fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    fun navigateBack() {
        findNavController().popBackStack()
    }

}