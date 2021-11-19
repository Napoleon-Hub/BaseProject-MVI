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
import com.baseproject.domain.enums.Difficulty
import com.baseproject.domain.enums.SocialStatus
import com.baseproject.utils.WIN_TERMINATOR_SCORE
import com.baseproject.utils.WIN_WEAKLING_SCORE
import com.baseproject.utils.extentions.*
import com.baseproject.view.base.BaseFragment
import com.baseproject.view.ui.game.adapter.GameRecyclerViewAdapter
import com.baseproject.view.ui.game.audio.GameAudioPlayer
import com.baseproject.view.ui.game.dialog.GameDialogFragment
import com.baseproject.view.ui.game.sheet.BottomSheetWinFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class GameFragment : BaseFragment(R.layout.fragment_game), DialogInterface.OnDismissListener {

    private lateinit var binding: FragmentGameBinding

    private val viewModel: GameViewModel by viewModels()

    @Inject
    lateinit var mediaPlayer: GameAudioPlayer

    private val adapter by lazy { GameRecyclerViewAdapter() }

    private var backPressedCallback: OnBackPressedCallback? = null

    private var scoreCounter = 0
    private var wordCounter = 0
    private var correctWordsArray: Array<String>? = null
    private var randomWordsArray: Array<String>? = null
    private var countDownTimer: CountDownTimer? = null
    private var timeLeft: Long = 27000L
    private var isSoundStarted = false
    private var bonusTime: Long = BONUS_INTERVAL_WEAKLING


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        correctWordsArray = resources.getStringArray(R.array.game_words_array)
        randomWordsArray = correctWordsArray?.copyOf()
        randomWordsArray?.shuffle()

        binding = FragmentGameBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (isSoundStarted) {
            mediaPlayer.resume()
        } else isSoundStarted = true
    }

    override fun onResume() {
        super.onResume()
        backPressedCallback = requireActivity()
            .onBackPressedDispatcher
            .addCallback {
                if (viewModel.uiState.value != GameContract.State.PreparationGameState)
                viewModel.setEvent(GameContract.Event.OnAutoLoseClick(scoreCounter, resources))
                else navigateBack()
            }
    }

    override fun onPause() {
        super.onPause()
        backPressedCallback?.remove()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        mediaPlayer.reset()
    }

    override fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    GameContract.State.PreparationGameState -> {
                        startTimer()
                    }
                    GameContract.State.StartGameState -> {
                        if (viewModel.difficulty == Difficulty.TERMINATOR) bonusTime = BONUS_INTERVAL_TERMINATOR
                        initUI()
                        initSetOnClickListeners()
                        showGameViews()
                        recreateCountDownTimer()
                        mediaPlayer.playMusic()
                        mediaPlayer.setMuted(viewModel.getMutedState())
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    GameContract.Effect.ShowLoseDialog -> {
                        countDownTimer?.cancel()
                        mediaPlayer.playLosePhrase()
                        showLoseDialog()
                    }
                    GameContract.Effect.ShowWinBottomSheetDialog -> {
                        countDownTimer?.cancel()
                        mediaPlayer.playWinPhrase()
                        showWinBottomSheetDialog()
                    }
                    GameContract.Effect.ShowAchieveToast -> {
                        toast(R.string.achievements_toast_text)
                    }
                    is GameContract.Effect.ChangeMuteState -> {
                        setButtonState(it.isMuted)
                    }
                }
            }
        }
    }

    override fun initUI() {
        setupGameRecyclerView()
        setButtonState(viewModel.getMutedState())
    }

    override fun initSetOnClickListeners() = binding.run {
        btnGameStop.setOnClickListener(500) {
            viewModel.setEvent(GameContract.Event.OnAutoLoseClick(scoreCounter, resources))
        }
        btnMute.setOnClickListener(500) {
            viewModel.setEvent(GameContract.Event.OnMuteClick)
        }
    }

    private fun startTimer() = binding.run {
        timer.apply {
            onFinished = ::onTimerFinish
            start(START_TIME)
        }
        val neededScores =  if (viewModel.difficulty == Difficulty.TERMINATOR) WIN_TERMINATOR_SCORE else WIN_WEAKLING_SCORE
        tvRules.text = getString(R.string.game_rules, neededScores)
    }

    private fun onTimerFinish() {
        viewModel.setEvent(GameContract.Event.GameStarted)
    }

    private fun setupGameRecyclerView() {
        binding.rvGameItems.adapter = adapter
        adapter.listener = {
            if (correctWordsArray!![wordCounter] == it) {
                scoreCounter++
                wordCounter++
                if (wordCounter == 9) {
                    updateWords()
                    countDownTimer?.cancel()
                    recreateCountDownTimer()
                }
            } else {
                viewModel.setEvent(GameContract.Event.OnAutoLoseClick(scoreCounter, resources))
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

    private fun showGameViews() = binding.run {
        tvRules.gone()
        timer.gone()
        btnMute.visible()
        tvCounter.visible()
        rvGameItems.visible()
        btnGameStop.visible()
    }

    private fun recreateCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeft + bonusTime, TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCounter.text = transformTimeToString(millisUntilFinished)
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                if (binding.rvGameItems.isEnabled) {
                    binding.rvGameItems.disable()
                    viewModel.setEvent(GameContract.Event.GameFinished(scoreCounter, resources))
                }
            }
        }.start()
    }

    // Called magic numbers, don't do that, but I can)
    private fun transformTimeToString(millisUntilFinished: Long): String {
        val seconds: Int
        var minutesString = MINUTES_NULLS

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
        val scorePartString = getString(R.string.game_score, scoreCounter)
        val descriptionPartString = when (viewModel.status) {
            SocialStatus.ALCOHOLIC  -> getString(R.string.game_lose_alcoholic_description)
            SocialStatus.PREGNANT   -> getString(R.string.game_lose_pregnant_description)
            SocialStatus.NAZI       -> getString(R.string.game_lose_nazi_description)
            SocialStatus.LUKASHENKA -> getString(R.string.game_lose_lukashenka_description)
            SocialStatus.KITTY      -> getString(R.string.game_lose_kitty_description)
            SocialStatus.ON_PILLS   -> getString(R.string.game_lose_on_pills_description)
            else                    -> getString(R.string.game_lose_bogdan_description)
        }
        val dialog = GameDialogFragment(status = "$scorePartString\n$descriptionPartString",listener = this@GameFragment)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
    }

    private fun showWinBottomSheetDialog() {
        val scorePartString = getString(R.string.game_score, scoreCounter)
        val descriptionPartString = when (viewModel.status) {
            SocialStatus.ALCOHOLIC  -> getString(R.string.game_win_alcoholic_description)
            SocialStatus.PREGNANT   -> getString(R.string.game_win_pregnant_description)
            SocialStatus.NAZI       -> getString(R.string.game_win_nazi_description)
            SocialStatus.LUKASHENKA -> getString(R.string.game_win_lukashenka_description)
            SocialStatus.KITTY      -> getString(R.string.game_win_kitty_description)
            SocialStatus.ON_PILLS   -> getString(R.string.game_win_on_pills_description)
            else                    -> getString(R.string.game_win_bogdan_description)
        }
        val dialog = BottomSheetWinFragment(status = "$scorePartString\n$descriptionPartString", listener = this@GameFragment)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
    }

    private fun setButtonState(isMuted: Boolean) {
        binding.imgMute.setImageResource(if (isMuted) R.drawable.ic_mute_off else R.drawable.ic_mute)
        mediaPlayer.setMuted(isMuted)
    }

    override fun onDismiss(dialog: DialogInterface?) { navigateBack() }

    companion object {
        const val START_TIME = 3
        const val TICK_INTERVAL = 1000L
        const val BONUS_INTERVAL_WEAKLING = 4000L
        const val BONUS_INTERVAL_TERMINATOR = 3000L
        const val MINUTES_NULLS = "00"
    }

}