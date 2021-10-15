package com.baseproject.view.ui.game

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baseproject.R
import com.baseproject.databinding.FragmentGameBinding
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.utils.extentions.*
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.game.adapter.GameRecyclerViewAdapter
import com.baseproject.view.ui.game.dialog.ResultGameDialogFragment
import com.baseproject.view.ui.game.sheet.BottomSheetWinFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GameFragment : BaseFragment(R.layout.fragment_game), DialogInterface.OnDismissListener {

    private lateinit var binding: FragmentGameBinding

    private val viewModel: GameViewModel by viewModels()

    private val adapter by lazy { GameRecyclerViewAdapter() }

    private var backPressedCallback: OnBackPressedCallback? = null

    private var scoreCounter = 0
    private var wordCounter = 0
    private var correctWordsArray: Array<String>? = null
    private var randomWordsArray: Array<String>? = null
    private var countDownTimer: CountDownTimer? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        correctWordsArray = resources.getStringArray(R.array.game_words_array)
        randomWordsArray = correctWordsArray?.copyOf()
        randomWordsArray?.shuffle()

        binding = FragmentGameBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        backPressedCallback = requireActivity()
            .onBackPressedDispatcher
            .addCallback {
                if (viewModel.uiState.value != GameContract.State.PreparationGameState)
                viewModel.setEvent(GameContract.Event.OnAutoLoseClicked(scoreCounter))
                else navigateBack()
            }
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback?.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }

    override fun initUI() {
        setupGameRecyclerView()
    }

    override fun initSetOnClickListeners() = binding.run {
        btnAutoLose.setOnClickListener(500) {
            viewModel.setEvent(GameContract.Event.OnAutoLoseClicked(scoreCounter))
        }
    }

    override fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    GameContract.State.PreparationGameState -> {
                        startTimer()
                    }
                    GameContract.State.StartGameState -> {
                        showGameViews()
                        createCountDownTimer()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    GameContract.Effect.ShowLoseDialog -> {
                        countDownTimer?.cancel()
                        showLoseDialog()
                    }
                    GameContract.Effect.ShowWinBottomSheetDialog -> {
                        countDownTimer?.cancel()
                        showWinBottomSheetDialog()
                    }
                }
            }
        }
    }

    private fun setupGameRecyclerView() {
        binding.rvGameItems.adapter = adapter
        adapter.listener = {
            if (correctWordsArray!![wordCounter] == it) {
                scoreCounter++
                wordCounter++
                if (wordCounter == 9) { updateWords() }
            } else {
                viewModel.setEvent(GameContract.Event.OnAutoLoseClicked(scoreCounter))
            }
        }
        adapter.submitList(randomWordsArray?.toList())
    }

    private fun updateWords() {
        wordCounter = 0
        randomWordsArray?.shuffle()
        adapter.submitList(randomWordsArray?.toList())
        adapter.notifyItemRangeChanged(0, 9)
    }

    private fun startTimer() {
        binding.timer.apply {
            onFinished = ::onTimerFinish
            start(START_TIME)
        }
    }

    private fun onTimerFinish() {
        viewModel.setEvent(GameContract.Event.GameStarted)
    }

    private fun showGameViews() = binding.run {
        tvRules.gone()
        timer.gone()
        tvCounter.visible()
        rvGameItems.visible()
        btnAutoLose.visible()
    }

    private fun createCountDownTimer() {
        countDownTimer = object : CountDownTimer(GAME_TIME, TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCounter.text = transformTimeToString(millisUntilFinished)
            }

            override fun onFinish() {
                viewModel.setEvent(GameContract.Event.GameFinished(scoreCounter))
            }
        }.start()
    }

    private fun transformTimeToString(millisUntilFinished: Long): String {
        val seconds: Int
        var minutesString = "00"

        if (millisUntilFinished >= 60000L) {
            val minutes = (millisUntilFinished / 60000L).toInt()
            minutesString = if (minutes < 10) ("0$minutes") else minutes.toString()
            seconds = (((millisUntilFinished / 60000L) - minutes) * 60).toInt()
        } else {
            seconds = ((millisUntilFinished * 60) / 60000L).toInt()
        }

        val secondsString: String = if (seconds < 10) ("0$seconds") else seconds.toString()
        return ("$minutesString:$secondsString")
    }

    private fun showLoseDialog() {
        val description = when (viewModel.getUserStatus()) {
            SocialStatus.ALCOHOLIC -> getString(R.string.game_lose_alcoholic_description)
            SocialStatus.PREGNANT -> getString(R.string.game_lose_pregnant_description)
            else -> getString(R.string.game_lose_nazi_description)
        }
        ResultGameDialogFragment(description, this@GameFragment).show(childFragmentManager, null)
    }

    private fun showWinBottomSheetDialog() {
        val description = when (viewModel.getUserStatus()) {
            SocialStatus.ALCOHOLIC -> getString(R.string.game_win_alcoholic_description)
            SocialStatus.PREGNANT -> getString(R.string.game_win_pregnant_description)
            else -> getString(R.string.game_win_nazi_description)
        }
        BottomSheetWinFragment(description, this@GameFragment).show(childFragmentManager, null)
    }

    override fun onDismiss(p0: DialogInterface?) {
        navigateBack()
    }

    companion object {
        const val START_TIME = 3
        const val GAME_TIME = 60000L
        const val TICK_INTERVAL = 1000L
    }

}