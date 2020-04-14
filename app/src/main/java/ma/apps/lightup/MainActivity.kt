package ma.apps.lightup

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
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
        findViewById<GameButton>(R.id.startGame).setOnClickListener {
            startGame()
        }
        findViewById<GameButton>(R.id.levels).setOnClickListener {
            openLevels()
        }
        findViewById<GameButton>(R.id.exitButton).setOnClickListener {
            finish()
        }
    }

    private fun openLevels() {
        startActivity(Intent(this, SelectSizeActivity::class.java))
    }

    private fun startGame() {
        val size = App.cache.getLastPlayedSize()
        val level = App.cache.loadLastLevel(size)
        startActivity(Intent(this, GameActivity::class.java)
            .putExtra("size", size)
            .putExtra("level", level))
    }
}
