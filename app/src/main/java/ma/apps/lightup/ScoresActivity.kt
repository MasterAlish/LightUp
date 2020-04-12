package ma.apps.lightup

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ma.apps.lightup.views.GameText

class ScoresActivity : AppCompatActivity() {
    private lateinit var scoresContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_scores)

        initUI()
        loadScores()
    }

    private fun loadScores() {
        val scores = App.cache.loadScores()

        if (scores.isNotEmpty()) {
            scoresContainer.removeAllViews()
            for (score in scores.withIndex()) {
                val view =
                    LayoutInflater.from(this).inflate(R.layout.score_item, scoresContainer, false)
                view.findViewById<GameText>(R.id.nameLabel)
                    .setText("${score.index + 1}. ${score.value.name}")
                view.findViewById<GameText>(R.id.levelLabel).setText(score.value.points.toString())
                scoresContainer.addView(view)
            }
        }
    }

    private fun initUI() {
        scoresContainer = findViewById(R.id.scoresContainer)

        findViewById<View>(R.id.close).setOnClickListener {
            finish()
        }
    }
}
