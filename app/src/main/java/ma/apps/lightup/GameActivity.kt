package ma.apps.lightup

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ma.apps.lightup.game.Game
import ma.apps.lightup.game.LinesGameListener
import ma.apps.lightup.views.GameText
import java.util.*

class GameActivity : AppCompatActivity() {
    private lateinit var gameField: LinearLayout
    private lateinit var game: Game
    private lateinit var scoreLabel: GameText
    private lateinit var timeLabel: GameText
    private val sounds = GameSounds()
    private val timer = Timer()
    private var totalSeconds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_game)
        initGame()
        initUI()
        initAnimations()
        sounds.init(this)

        game.startGame()
        startTimer()
    }

    private fun initGame() {
        game = Game(gameListener)
    }

    private fun initUI() {
        gameField = findViewById(R.id.gameField)
        initGameField(gameField, game)

        scoreLabel = findViewById(R.id.scoreLabel)
        timeLabel = findViewById(R.id.timeLabel)

        updateTime()
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                totalSeconds += 1
                runOnUiThread {
                    updateTime()
                }
            }
        }, 1000, 1000)
    }

    private fun updateTime() {
        var formatted = totalSeconds.toString()

        if(totalSeconds >= 60){
            formatted = "${totalSeconds / 60}:${(totalSeconds % 60).toString().padStart(2, '0')}"
        }

        timeLabel.setText(getString(R.string.time_s, formatted))
    }

    private val onCellClickListener = View.OnClickListener { cell ->

    }

    private val gameListener = object : LinesGameListener {

    }

    private fun initGameField(gameField: LinearLayout, game: Game) {

    }

    private fun initAnimations() {
        val cellPadding = App.dimens.dpToPx(3f)

        val set = AnimationSet(true)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.duration = 200

        val jump = TranslateAnimation(0f, 0f, -cellPadding.toFloat(), cellPadding.toFloat())
        jump.repeatMode = Animation.REVERSE
        jump.repeatCount = Animation.INFINITE
        jump.duration = 200
        set.addAnimation(jump)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@GameActivity)
        val dialog = builder
            .setTitle(R.string.app_name)
            .setMessage(R.string.do_you_want_finish_this_game)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { _, _ ->  }
            .create()
        dialog.show()
    }
}