package pl.dzazef.hangman

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

fun Array<String>.random(): String = if (size > 0) get(Random().nextInt(size)) else ""
fun String.addSpaces(): String {
    var out = ""
    for (i in this) {
        out = "$out$i "
    }
    return out
}

class WordManager(private val context: Context) {
    val guessedLetters : List<Char> = mutableListOf('a', 'e', 'i', 'o', 'u')
    var currentWord : String = randomWord()
    var dashedWord : String = wordToDash(currentWord, guessedLetters)

    fun randomWord() : String = context.resources.getStringArray(R.array.words).random().addSpaces()
    fun wordToDash(word: String, letters: List<Char>): String {
        var newWord = word
        for (l in newWord) {
            if (l !in letters && l!=' ') {
                newWord = newWord.replace(l.toString(), "_")
            }
        }
        return newWord
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val wordManager = WordManager(this)
        Log.d("INFO1", wordManager.currentWord)
        Log.d("INFO1", wordManager.dashedWord)
    }
}
