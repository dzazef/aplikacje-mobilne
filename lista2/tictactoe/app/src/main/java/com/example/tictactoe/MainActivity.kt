package com.example.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

val userSymbol = State.X
val computerSymbol = State.O

var userScore = 0
var computerScore = 0

enum class State {
    O, X, EMPTY;
}

open class FieldManager {
    private var field = 0
    val fieldMap : MutableMap<Int, State> = mutableMapOf()
    val buttonSet : MutableSet<Pair<Int, Button>> = mutableSetOf()
    var weightMap : MutableMap<Int, Int> = mutableMapOf(
        0 to 12, 1 to 8, 2 to 8, 3 to 8, 4 to 12,
        5 to 8, 6 to 12, 7 to 8, 8 to 12, 9 to 8,
        10 to 8, 11 to 8, 12 to 16, 13 to 8, 14 to 8,
        15 to 8, 16 to 12, 17 to 8, 18 to 12, 19 to 8,
        20 to 12, 21 to 8, 22 to 8, 23 to 8, 24 to 12)

    fun addField() {
        fieldMap[field++] = State.EMPTY
    }

    fun updateFields(id: Int, s: State) {
        fieldMap[id] = s
    }

    fun getStateById(id: Int): State? {
        return fieldMap[id]
    }
}

class GameManager : FieldManager(){
    var userTurn = true

    private val winTactics : List<List<Int>> =
        listOf(
            listOf(0, 1, 2, 3, 4),
            listOf(5, 6, 7, 8, 9),
            listOf(10, 11, 12, 13, 14),
            listOf(15, 16, 17, 18, 19),
            listOf(20, 21, 22, 23, 24),
            listOf(0, 5, 10, 15, 20),
            listOf(1, 6, 11, 16, 21),
            listOf(2, 7, 12, 17, 22),
            listOf(3, 8, 13, 18, 23),
            listOf(4, 9, 14, 19, 24),
            listOf(0, 6, 12, 18, 21),
            listOf(0, 6, 12, 18, 24),
            listOf(4, 8, 12, 16, 20))

    fun checkWin() : State {
        val idXList = fieldMap.filter { (_, s) -> s == State.X }.keys.toList()
        val idOList = fieldMap.filter { (_, s) -> s == State.O }.keys.toList()
        for (tactic in winTactics) {
            if (idXList.containsAll(tactic)) return State.X
            if (idOList.containsAll(tactic)) return State.O
        }
        return State.EMPTY
    }

    fun checkIfPlayerIsWinning(): Int? {
        val allPlayerFields = fieldMap.filter { it.value== userSymbol }.keys
        for (tactic in winTactics) {
            val intersect = allPlayerFields.intersect(tactic)
            if (intersect.size==4) {
                val possibleField = tactic.minus(intersect).first()
                if (fieldMap[possibleField]==State.EMPTY) {
                    Log.d("INFO", "User is almost winning...")
                    Log.d("TACTIC", "Choose field no $possibleField")
                    return possibleField
                }
            }
        }
        return null
    }

    fun bestLine(): List<Int>? {
        val computerFields = fieldMap.filter { it.value== computerSymbol }.keys
        val userFields = fieldMap.filter { it.value== userSymbol }.keys
        var maxLine : Pair<Int, Set<Int>>? = null
        for (i in 0..20 step 5) {
            if((i..i+4).intersect(userFields).isEmpty()) {
                val numOfSymbolInLine = (i..i+4).intersect(computerFields).size
                if (maxLine==null || numOfSymbolInLine > maxLine.first)
                    maxLine = Pair(numOfSymbolInLine, (i..i+4).toSet())
            }
        }
        for (i in 0..4) {
            if(setOf(i, i+5, i+10, i+15, i+20).intersect(userFields).isEmpty()) {
                val numOfSymbolInLine = setOf(i, i+5, i+10, i+15, i+20).intersect(computerFields).size
                if (maxLine==null || numOfSymbolInLine > maxLine.first)
                    maxLine = Pair(numOfSymbolInLine, setOf(i, i+5, i+10, i+15, i+20))
            }
        }
        if(setOf(0, 6, 12, 18, 24).intersect(userFields).isEmpty()) {
            val numOfSymbolInLine = setOf(0, 6, 12, 18, 24).intersect(computerFields).size
            if (maxLine==null || numOfSymbolInLine > maxLine.first)
                maxLine = Pair(numOfSymbolInLine, setOf(0, 6, 12, 18, 24))
        }
        if(setOf(4, 8, 12, 16, 20).intersect(userFields).isEmpty()) {
            val numOfSymbolInLine = setOf(4, 8, 12, 16, 20).intersect(computerFields).size
            if (maxLine==null || numOfSymbolInLine > maxLine.first)
                maxLine = Pair(numOfSymbolInLine, setOf(4, 8, 12, 16, 20))
        }
        Log.d("INFO", "best line: "+maxLine?.second?.toString())
        return maxLine?.second?.toList()
    }

    fun chooseField(): Int? {
        val field = checkIfPlayerIsWinning()
        if (field!=null) {
            return field
        } else {
            var bestLine = bestLine()
            if (bestLine!=null) {
                bestLine = bestLine.filter { e -> fieldMap[e]==State.EMPTY } //wyrzuć niepotrzebne pola
                var pair : Pair<Int, Int>? = null
                for (f in bestLine) {
                    if (pair==null) {
                        pair = Pair(f, weightMap[f]!!)
                    } else {
                        if (weightMap[f]!! > pair.second) {
                            pair = Pair(f, weightMap[f]!!)
                        }
                    }
                }
                return pair?.first
            } else {
                val allPossibleFields = fieldMap.filter { it.value==State.EMPTY }.keys
                var pair : Pair<Int, Int>? = null
                for (f in allPossibleFields) {
                    if (pair==null) {
                        pair = Pair(f, weightMap[f]!!)
                    } else {
                        if (weightMap[f]!! > pair.second) {
                            pair = Pair(f, weightMap[f]!!)
                        }
                    }
                }
                return pair?.first
            }
        }
    }

    fun userClick(b: Button) {
        val id = buttonSet.first { it.second==b }.first
        if (fieldMap[id]==State.EMPTY) {
            updateFields(id, userSymbol)
            b.text = userSymbol.toString()

            val field = chooseField()
            if (field!=null) {
                Log.d("BOT", "chosenField: $field")
                updateFields(field, computerSymbol)
                buttonSet.first { it.first==field }.second.text = getStateById(field).toString()
            } else {
                Log.d("BOT", "Can't find any field! Perhaps the game is over?")
            }
        }
    }

    fun resetFields() {
        fieldMap.map { updateFields(it.key, State.EMPTY) }
        buttonSet.forEach { it.second.text = "" }
    }
}



val g = GameManager()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..24)
            g.addField()
        g.buttonSet.addAll(setOf(
            0 to button1_1, 1 to button1_2, 2 to button1_3, 3 to button1_4, 4 to button1_5,
            5 to button2_1, 6 to button2_2, 7 to button2_3, 8 to button2_4, 9 to button2_5,
            10 to button3_1, 11 to button3_2, 12 to button3_3, 13 to button3_4, 14 to button3_5,
            15 to button4_1, 16 to button4_2, 17 to button4_3, 18 to button4_4, 19 to button4_5,
            20 to button5_1, 21 to button5_2, 22 to button5_3, 23 to button5_4, 24 to button5_5
        ))
    }

    fun checkLog(view: View) {
        g.checkIfPlayerIsWinning()
        g.bestLine()
    }

    fun onClick(view: View) {
        val button = findViewById<Button>(view.id)
        g.userClick(button)
        when (g.checkWin()) {
            State.EMPTY -> Unit
            State.X -> {
                if (computerSymbol==State.X) {
                    computerScore++
                } else {
                    userScore++
                }
                scoreText.text = "User: $userScore\nBot: $computerScore"
                g.resetFields()
            }
            State.O ->  {
                if (computerSymbol==State.O) {
                    computerScore++
                } else {
                    userScore++
                }
                scoreText.text = "User: $userScore\nBot: $computerScore"
                g.resetFields()
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onResetClick(view: View) {
        g.resetFields()
    }
}