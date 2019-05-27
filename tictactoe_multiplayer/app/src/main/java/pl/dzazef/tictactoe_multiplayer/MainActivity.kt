package pl.dzazef.tictactoe_multiplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_start.setOnClickListener { onStartClick() }
        main_btn_login.setOnClickListener { onLoginClick() }
    }

    private fun onStartClick() {
        val newGameIntent = Intent(this, GameActivity::class.java)
        startActivity(newGameIntent)
    }

    private fun onLoginClick() {

    }
}
