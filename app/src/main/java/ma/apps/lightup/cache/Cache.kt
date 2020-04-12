package ma.apps.lightup.cache

import android.app.Application
import android.content.Context
import java.lang.Exception
import java.lang.StringBuilder

class Cache(val app: Application) {
    private val LinesDelimiter = ";;;;"
    private val Delimiter = "::::"

    fun loadScores(): List<Score> {
        val prefs = app.getSharedPreferences("ScoresPrefs", Context.MODE_PRIVATE)
        val scoresStr = prefs.getString("Scores", "")!!.split(";;;;")

        val scores = mutableListOf<Score>()

        for(scoreStr in scoresStr) {
            try {
                val data = scoreStr.split(Delimiter)
                scores.add(Score(data[0], data[1].toInt()))
            }catch (e:Exception){}
        }
        return scores
    }

    fun saveScore(newScore: Score){
        var scores = loadScores().toMutableList()
        scores.add(newScore)
        scores.sortBy { -it.points }
        if(scores.size > 10) {
            scores = scores.subList(0, 10)
        }

        val scoresStr = StringBuilder()

        scores.forEach { score ->
            scoresStr.append(score.name)
            scoresStr.append(Delimiter)
            scoresStr.append(score.points)
            scoresStr.append(LinesDelimiter)
        }

        val prefs = app.getSharedPreferences("ScoresPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("Scores", scoresStr.toString()).apply()
    }
}