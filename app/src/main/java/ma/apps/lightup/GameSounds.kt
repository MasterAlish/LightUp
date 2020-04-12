package ma.apps.lightup

import android.content.Context
import android.media.SoundPool

class GameSounds {
    private val EXPLODE = 1
    private val MOVE = 2
    private val REJECT = 3
    private val SELECT = 4

    private val soundPool = SoundPool.Builder().setMaxStreams(2).build()
    private val sounds = mutableMapOf<Int, Int>()

    fun init(context: Context){
        sounds[EXPLODE] = soundPool.load(context, R.raw.explode, 1)
        sounds[SELECT] = soundPool.load(context, R.raw.click, 2)
        sounds[MOVE] = soundPool.load(context, R.raw.move, 3)
        sounds[REJECT] = soundPool.load(context, R.raw.reject, 4)
    }

    fun onSelect(){
        play(SELECT)
    }

    fun onMove(){
        play(MOVE)
    }

    fun onReject(){
        play(REJECT)
    }

    fun onExplode(){
        play(EXPLODE)
    }

    private fun play(soundId: Int){
        val volume = 0.5f
        soundPool.play(sounds[soundId]!!, volume, volume, 1, 0, 1f)
    }
}