package pl.dzazef.tictactoe_multiplayer.game

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_game.*
import pl.dzazef.tictactoe_multiplayer.R
import pl.dzazef.tictactoe_multiplayer.SIZE
import pl.dzazef.tictactoe_multiplayer.firebase.db.DatabaseManager

class GameActivity : AppCompatActivity() {

    private val databaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setUpActionBar()
        generateLayout()
        databaseManager.lookForRooms()
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
                button.setOnClickListener { onFieldClick(it) }
                buttonIndex++
                tableRow.addView(button)
            }
        }
    }

    private fun onFieldClick(v: View) {
        Log.i("INFO1", v.tag.toString())
    }
}
