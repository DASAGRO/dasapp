package kz.das.dasaccounting.ui.utils

import android.content.Context
import android.media.MediaPlayer
import kz.das.dasaccounting.R

object MediaPlayerUtils {
    fun playSuccessSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.success_sound_effect)
        mediaPlayer.isLooping = false
        mediaPlayer.start()
    }
}