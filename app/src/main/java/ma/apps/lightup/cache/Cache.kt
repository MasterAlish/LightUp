package ma.apps.lightup.cache

import android.app.Application
import android.content.Context

class Cache(val app: Application) {
    fun loadLastLevel(size: Int): Int {
        val prefs = app.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("Last_For_Size_$size", 0)
    }

    fun saveLastLevel(size: Int, level: Int) {
        val prefs = app.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("Last_For_Size_$size", level).apply()
    }

    fun getLastPlayedSize(): Int {
        val prefs = app.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE)
        return prefs.getInt("Last_Played_Size", 7)
    }

    fun setLastPlayedSize(size: Int) {
        val prefs = app.getSharedPreferences("LevelPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("Last_Played_Size", size).apply()
    }

    fun isDemoPlayed(): Boolean {
        val prefs = app.getSharedPreferences("Demo", Context.MODE_PRIVATE)
        return prefs.getBoolean("Demo_Played", false)
    }

    fun setDemoPlayed() {
        val prefs = app.getSharedPreferences("Demo", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("Demo_Played", true).apply()
    }
}