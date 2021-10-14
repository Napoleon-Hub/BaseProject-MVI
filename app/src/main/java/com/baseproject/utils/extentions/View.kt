package com.baseproject.utils.extentions

import android.view.View

fun View.setOnClickListener(clickInterval: Long, callback: (View) -> Unit) =
    setOnClickListener(OnClickListener(clickInterval, callback))

class OnClickListener(
    private val interval: Long,
    private val callback: (View) -> Unit
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val time = System.currentTimeMillis()
        if (time - lastClickTime >= interval) {
            lastClickTime = time
            callback(v)
        }
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}