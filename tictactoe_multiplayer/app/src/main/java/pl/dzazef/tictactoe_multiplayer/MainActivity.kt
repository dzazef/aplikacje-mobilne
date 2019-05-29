package pl.dzazef.tictactoe_multiplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import pl.dzazef.tictactoe_multiplayer.dialog.UserNotSignedInFragment
import pl.dzazef.tictactoe_multiplayer.firebase.auth.LoginManager
import pl.dzazef.tictactoe_multiplayer.game.GameActivity

class MainActivity : AppCompatActivity() {

    private val loginManager = LoginManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_start.setOnClickListener { onStartClick() }
        main_btn_login.setOnClickListener { onLoginClick() }

        Log.i("INFO1", FirebaseAuth.getInstance().currentUser.toString())
    }

    private fun onStartClick() {
        if (loginManager.getUser() == null) {
            UserNotSignedInFragment().show(supportFragmentManager, "dialog")
        } else {
            val newGameIntent = Intent(this, GameActivity::class.java)
            startActivity(newGameIntent)
        }
    }

    private fun onLoginClick() {
        loginManager.signIn()
    }

    private fun onSignOutClick() {
        loginManager.signOut()
    }
}
