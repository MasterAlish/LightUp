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
        findViewById<GameButton>(R.id.scores).setOnClickListener {
            openScores()
        }
        findViewById<GameButton>(R.id.howToPlay).setOnClickListener {
            openHelp()
        }
        findViewById<GameButton>(R.id.exitButton).setOnClickListener {
            finish()
        }
    }

    private fun openScores() {
        startActivity(Intent(this, ScoresActivity::class.java))
    }

    private fun openHelp() {
        startActivity(Intent(this, HelpActivity::class.java))
    }

    private fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }
}
