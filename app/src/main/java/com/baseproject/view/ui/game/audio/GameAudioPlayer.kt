package com.baseproject.view.ui.game.audio

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAudioPlayer @Inject constructor(@ApplicationContext val context: Context) {

    private var playlist = mutableListOf<SoundMediaItem>()
    private var playerState = PlayerState.PLAYER_STATE_STOPPED

    private var mediaPlayer = MediaPlayer().apply {

        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        setOnPreparedListener {
            it.start()
        }

        setOnCompletionListener {
            it.reset()
            if (playlist.isNotEmpty()) {
                playMedia(playlist[0])
            } else {
                playerState = PlayerState.PLAYER_STATE_STOPPED
            }
        }

    }

    private fun play() {
        if (playerState != PlayerState.PLAYER_STATE_PLAYING && playlist.isNotEmpty())
            playMedia(playlist[0])
    }

    fun setMuted(state: Boolean) {
        val volume = if (state) 0f else 1f
        mediaPlayer.setVolume(volume, volume)
    }

    fun pause() {
        playerState = PlayerState.PLAYER_STATE_PAUSED
        mediaPlayer.pause()
    }

    fun resume() {
        playerState = PlayerState.PLAYER_STATE_PLAYING
        mediaPlayer.start()
    }

    fun reset() {
        playlist.clear()
        mediaPlayer.reset()
        playerState = PlayerState.PLAYER_STATE_STOPPED
    }

//    fun playRandomPhrase() {
//        playlist.add(
//            SoundMediaItem(
//                SoundType.SOUND_TYPE_PHRASE,
//                phrasesList.random()
//            )
//        )
//        play()
//    }

    fun playMusic() {
        val music = musicList.random()
        playlist.addAll(
            listOf(
                SoundMediaItem(
                    SoundType.SOUND_TYPE_MUSIC,
                    music
                ),
                SoundMediaItem(
                    SoundType.SOUND_TYPE_MUSIC,
                    music
                )
            )
        )
        play()
    }

    fun playLosePhrase() {
        reset()
        playlist.add(
            SoundMediaItem(
                SoundType.SOUND_TYPE_FINISH_LOSE,
                loseList.random()
            )
        )
        play()
    }

    fun playWinPhrase() {
        reset()
        playlist.add(
            SoundMediaItem(
                SoundType.SOUND_TYPE_FINISH_WIN,
                winList.random()
            )
        )
        play()
    }

    private fun playMedia(soundItem: SoundMediaItem) {
        with(mediaPlayer) {
            playerState = PlayerState.PLAYER_STATE_PLAYING
            playlist.remove(soundItem)
            try {
                val filePath = StringBuilder()
                    .append("sounds/")
                    .append(soundItem.soundType())
                    .append(soundItem.soundItem())
                    .toString()
                val afd: AssetFileDescriptor = context.assets.openFd(filePath)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepareAsync()
            } catch (e: Exception) {
                playerState = PlayerState.PLAYER_STATE_STOPPED
                e.printStackTrace()
                play()
            }
        }
    }

    data class SoundMediaItem(
        val soundType: SoundType,
        val soundItem: SoundItem
    )

    companion object {

        enum class PlayerState {
            PLAYER_STATE_PLAYING,
            PLAYER_STATE_PAUSED,
            PLAYER_STATE_STOPPED
        }

        enum class SoundType(private val directory: String) {

            //            SOUND_TYPE_PHRASE("phrases"),
            SOUND_TYPE_MUSIC("music"),
            SOUND_TYPE_FINISH_LOSE("lose"),
            SOUND_TYPE_FINISH_WIN("win");

            operator fun invoke() = "$directory/"
        }

        enum class SoundItem(private val filename: String) {

            /* PHRASES */
//            BREAK_ME_COMPLETELY("break_me_completely"),
//            BRO_I_BROUGHT_YOU_SOME_EAT("bro_i_brought_you_some_eat"),
//            COME_ON_HERE("come_on_here"),
//            DONT_NEED_INTERNET("dont_need_internet"),
//            FLY_IS_ALSO_DUMPLING("fly_is_also_dumpling"),
//            GET_PUSSIES("get_pussies"),
//            GIVE_BACK_LARD("give_back_lard"),
//            HARD_TO_LIVE("hard_to_live"),
//            HELLO("hello"),
//            I_DONT_CALL_YOU("i_dont_call_you"),
//            IBRAHIM("ibrahim"),
//            IM_TIRED_OF_GOING("im_tired_of_going"),
//            MEAT_WITH_POTATO("meat_with_potato"),
//            MENT("ment"),
//            MINECRAFT("minecraft"),
//            NORMAL("normal"),
//            OUR_CREDENTIALS_ARE_ALL("our_credentials_are_all"),
//            PLAN("plan"),
//            POWER_OF_THE_EARTH("power_of_the_earth"),
//            SEARCH_ENGINE("search_engine"),
//            SILENCE("silence"),
//            SPARTA("sparta"),
//            TODAY_ON_TOMORROWS_DAY("today_on_tomorrows_day"),
//            TURN_OFF_CAMERA("turn_off_camera"),
//            VERY_SCARY("very_scary"),
//            WHAT_A_TWIST("what_a_twist"),
//            WHY_DID_I_COME_HERE("why_did_i_come_here"),

            /* MUSIC */
            JOHANN_SEBASTIAN_BACH("Johann Sebastian Bach - Badinerie"),
            LEE_JIN_WOOK("Lee Jin Wook - You Don't Love Me, But I Love You"),
            LUDOVICO_EINAUDI("Ludovico Einaudi - Life"),

            /* LOSE */
            CASINO("casino"),
            END_OF_THE_FILM("end_of_the_film"),
            FUCK("fuck"),
            GO_SLEEP("go_sleep"),
            I_BEAT_YOU("i_beat_you"),
            LETS_DO_IT_AGAIN("lets_do_it_again"),
            LOH("loh"),
            LOVE_LIFE("love_life"),
            OH_NO("oh_no"),
            TITANIC("titanic"),
            TYPICAL_EXAMPLE_OF_MORON("typical_example_of_moron"),
            YOU_MADE_ME_LAUGH("you_made_me_laugh"),

            /* WIN */
            BAKLAJAN("baklajan"),
            DEMOTIVATOR("demotivator"),
            ERALASH("eralash"),
            GTA("gta"),
            I_JUST_CLAP("i_just_clap"),
            OMG("omg"),
            SONG_FOR_BOYS("song_for_boys"),
            TU_TU_TU("tututu");

            operator fun invoke() = "$filename.mp3"
        }

//        val phrasesList = listOf(
//            SoundItem.BREAK_ME_COMPLETELY,
//            SoundItem.BRO_I_BROUGHT_YOU_SOME_EAT,
//            SoundItem.COME_ON_HERE,
//            SoundItem.DONT_NEED_INTERNET,
//            SoundItem.FLY_IS_ALSO_DUMPLING,
//            SoundItem.GET_PUSSIES,
//            SoundItem.GIVE_BACK_LARD,
//            SoundItem.HARD_TO_LIVE,
//            SoundItem.HELLO,
//            SoundItem.I_DONT_CALL_YOU,
//            SoundItem.IBRAHIM,
//            SoundItem.IM_TIRED_OF_GOING,
//            SoundItem.MEAT_WITH_POTATO,
//            SoundItem.MENT,
//            SoundItem.MINECRAFT,
//            SoundItem.NORMAL,
//            SoundItem.OUR_CREDENTIALS_ARE_ALL,
//            SoundItem.PLAN,
//            SoundItem.POWER_OF_THE_EARTH,
//            SoundItem.SEARCH_ENGINE,
//            SoundItem.SILENCE,
//            SoundItem.SPARTA,
//            SoundItem.TODAY_ON_TOMORROWS_DAY,
//            SoundItem.TURN_OFF_CAMERA,
//            SoundItem.VERY_SCARY,
//            SoundItem.WHAT_A_TWIST,
//            SoundItem.WHY_DID_I_COME_HERE
//        )

        val musicList = listOf(
            SoundItem.JOHANN_SEBASTIAN_BACH,
            SoundItem.LEE_JIN_WOOK,
            SoundItem.LUDOVICO_EINAUDI
        )

        val loseList = listOf(
            SoundItem.CASINO,
            SoundItem.END_OF_THE_FILM,
            SoundItem.FUCK,
            SoundItem.GO_SLEEP,
            SoundItem.I_BEAT_YOU,
            SoundItem.LETS_DO_IT_AGAIN,
            SoundItem.LOH,
            SoundItem.LOVE_LIFE,
            SoundItem.OH_NO,
            SoundItem.TITANIC,
            SoundItem.TYPICAL_EXAMPLE_OF_MORON,
            SoundItem.YOU_MADE_ME_LAUGH
        )

        val winList = listOf(
            SoundItem.BAKLAJAN,
            SoundItem.DEMOTIVATOR,
            SoundItem.ERALASH,
            SoundItem.GTA,
            SoundItem.I_JUST_CLAP,
            SoundItem.OMG,
            SoundItem.SONG_FOR_BOYS,
            SoundItem.TU_TU_TU
        )
    }
}