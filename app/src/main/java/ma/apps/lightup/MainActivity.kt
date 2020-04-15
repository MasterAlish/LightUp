package ma.apps.lightup

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import ma.apps.lightup.views.GameButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        val startBtn = findViewById<View>(R.id.startGame)
        animateStartBtn(startBtn)

        startBtn.setOnClickListener {
            startGame()
        }
        findViewById<GameButton>(R.id.levels).setOnClickListener {
            openLevels()
        }
        findViewById<GameButton>(R.id.exitButton).setOnClickListener {
            finish()
        }
    }

    private fun animateStartBtn(startBtn: View) {
        val rotate = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 160
        rotate.repeatMode = Animation.REVERSE
        rotate.repeatCount = Animation.INFINITE
        rotate.interpolator = DecelerateInterpolator()
        startBtn.startAnimation(rotate)
    }

    private fun openLevels() {
        startActivity(Intent(this, SelectSizeActivity::class.java))
    }

    private fun startGame() {
        val size = App.cache.getLastPlayedSize()
        val level = App.cache.loadLastLevel(size)
        startActivity(
            Intent(this, GameActivity::class.java)
                .putExtra("size", size)
                .putExtra("level", level)
        )
    }
}
