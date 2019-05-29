package pl.dzazef.tictactoe_multiplayer.game

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import pl.dzazef.tictactoe_multiplayer.R
import pl.dzazef.tictactoe_multiplayer.SIZE
import pl.dzazef.tictactoe_multiplayer.firebase.db.RoomFinder
import pl.dzazef.tictactoe_multiplayer.firebase.db.RoomManager
import java.lang.StringBuilder

class GameActivity : AppCompatActivity(), RoomManager.RoomManagerCallback {

    override fun setUser(user: String) {
        mySign = if (user == "user1")
            "X"
        else
            "O"
        game_txt_sign.text = mySign
    }

    override fun setScore(score: String) {
        for (i in 0 until score.length) {
            buttons[i].text = when (score[i]) {
                'X' -> "X"
                'O' -> "O"
                else -> ""
            }
        }
        checkScore()
    }

    private fun checkScore() {
        var previous = ""
        var current = ""
        var win = true
        //POZIOM
        for (i in 0 until SIZE*SIZE step SIZE) {
            previous = ""
            win = true
            for (j in i until i + SIZE) {
                current = buttons[j].text.toString()
                if (current != "" && (current == previous || previous == "")) {
                    previous = current
                } else {
                    win = false
                }
            }
            if (win) userWon(buttons[i].text.toString())
        }
        //PION
        for (i in 0 until SIZE) {
            previous = ""
            win = true
            for (j in i until SIZE*SIZE step SIZE) {
                current = buttons[j].text.toString()
                if (current != "" && (current == previous || previous == "")) {
                    previous = current
                } else {
                    win = false
                }
            }
            if (win) userWon(buttons[i].text.toString())
        }
        //SKOS1
        previous = ""
        win = true
        for (i in 0 until SIZE*SIZE step SIZE+1) {
            current = buttons[i].text.toString()
            if (current != "" && (current == previous || previous == "")) {
                previous = current
            } else {
                win = false
            }
        }
        if (win) userWon(buttons[0].text.toString())
        //SKOS2
        previous = ""
        win = true
        for (i in SIZE*SIZE-SIZE downTo 1 step SIZE-1) {
            current = buttons[i].text.toString()
            if (current != "" && (current == previous || previous == "")) {
                previous = current
            } else {
                win = false
            }
        }
        if (win) userWon(buttons[SIZE*SIZE-SIZE].text.toString())
    }

    private fun userWon(sign: String) {
        Toast.makeText(this, "$sign won", Toast.LENGTH_SHORT).show()
        roomManager.sendScore("UUUUUUUUU")
    }

    override fun setRound(round: Boolean) {
        this.myRound = round
    }

    override fun setPlaying(playing: Boolean) {
        this.playing = playing
    }

    private val roomManager = RoomManager(this, this)
    private var playing = false
    private var myRound = false
    private var mySign = ""
    private var buttons = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setUpActionBar()
        generateLayout()

        if (intent.getBooleanExtra("NEW_ROOM", true)) {
            roomManager.connect(null)
        } else {
            roomManager.connect(intent.getStringExtra("ID"))
        }
    }

    private fun setUpActionBar() {
        val actionBar = supportActionBar
        actionBar!!.title = "tic-tac-toe"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun generateLayout() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val buttonSize = (size.x*0.8) / SIZE
        var buttonIndex = 0
        for (i in 0 until SIZE) {
            val tableRow = TableRow(this)
            tableRow.gravity = Gravity.CENTER
            game_table.addView(tableRow)
            for (j in 0 until SIZE) {
                val button = Button(this)
                button.height = buttonSize.toInt()
                button.width = buttonSize.toInt()
                button.tag = buttonIndex
                button.textSize = 30f
                button.setOnClickListener { onFieldClick(it.tag.toString().toInt()) }
                buttonIndex++
                buttons.add(button)
                tableRow.addView(button)
            }
        }
    }

    private fun getScore(): String {
        val sb = StringBuilder()
        for (button in buttons) {
            when (button.text) {
                "X" -> sb.append("X")
                "O" -> sb.append("O")
                else -> sb.append("U")
            }
        }
        return sb.toString()
    }

    private fun onFieldClick(idx: Int) {
        if (playing && myRound && buttons[idx].text == "") {
            buttons[idx].text = mySign
        }
        roomManager.sendScore(getScore())
    }

    override fun onDestroy() {
        Log.d("DEBUG1", "tu")
        roomManager.removeRoom()
        super.onDestroy()
    }
}
