package com.baseproject.utils.timer

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownTimer(
    private var time: Int,
    private val period: Long = 1000L,
    private val onTick: (Int) -> Unit
) {
    private var job = Job()
    var onFinish: () -> Unit = { }

    fun start() {
        job = Job()
        MainScope().launch(job) {
            while (time >= 0){
                onTick(time)
                delay(period)
                time--
            }
            onFinish()
        }
    }

    fun pause() { job.cancel() }
}