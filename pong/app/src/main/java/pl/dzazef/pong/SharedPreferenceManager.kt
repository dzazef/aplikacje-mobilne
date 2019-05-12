package pl.dzazef.pong

import android.content.Context

class SharedPreferenceManager(context: Context) {

    private val sharedPref = context.getSharedPreferences("PONG", Context.MODE_PRIVATE)

    fun saveHighScore(score : Int) {
        val editor = sharedPref.edit()
        editor.putInt("HIGH_SCORE", score)
        editor.apply()
    }

    fun getHighScore() : Int = sharedPref.getInt("HIGH_SCORE", 0)
}