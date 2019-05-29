package pl.dzazef.tictactoe_multiplayer.firebase.auth

import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pl.dzazef.tictactoe_multiplayer.dialog.SignedOutFragment

const val RC_SIGN_IN = 9001
val providers = arrayListOf(
    AuthUI.IdpConfig.EmailBuilder().build()
)

class LoginManager(private val context : AppCompatActivity) {

    fun signIn() {
        context.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    fun signOut() {
        AuthUI.getInstance().signOut(context).addOnCompleteListener{signOutListener()}
    }

    private fun signOutListener() {
        SignedOutFragment().show(context.supportFragmentManager, "dialog")
    }

    fun getUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
}