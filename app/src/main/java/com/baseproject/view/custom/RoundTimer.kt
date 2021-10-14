package com.baseproject.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.baseproject.databinding.ViewTimerRoundBinding
import com.baseproject.utils.timer.CountdownTimer
import kotlin.math.abs

class RoundTimer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle){
    private var binding = ViewTimerRoundBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    private var timer: CountdownTimer? = null

    var onTick: (Int) -> Unit = {}
    var onFinished: () -> Unit = {}


    fun pause() = timer?.pause()

    fun resume() = timer?.start()

    fun cancel(){
        pause()
        this.visibility = INVISIBLE
    }

    fun start(time: Int){
        val numberOfTicks = time * 1000 / PROGRESS_DELAY
        binding.progressBar.max = numberOfTicks
        binding.progressBar.progress = 1
        binding.tvTime.text = "$time"
        timer = CountdownTimer(numberOfTicks, PROGRESS_DELAY.toLong()){ i ->
            binding.progressBar.progress = abs(i - numberOfTicks)
            if(i % (1000 / PROGRESS_DELAY) == 0) {
                val progress = (i * PROGRESS_DELAY / 1000 + 1)
                binding.tvTime.text = progress.toString()
                onTick(progress)
            }
        }
        timer!!.onFinish = {
            onFinished()
            this.visibility = View.INVISIBLE
        }
        this.visibility = View.VISIBLE
        timer!!.start()
    }

    companion object {
        const val PROGRESS_DELAY = 30
    }
}