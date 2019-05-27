package pl.dzazef.tictactoe_multiplayer

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setUpActionBar()
        generateLayout()
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
                buttonIndex++
                tableRow.addView(button)
            }
        }
    }
}
