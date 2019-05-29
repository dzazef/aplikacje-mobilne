package pl.dzazef.tictactoe_multiplayer.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

//USAGE: UserNotSignedInFragment().show(supportFragmentManager, "dialog")
class UserNotSignedInFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("You have to sign in before playing")
                .setNeutralButton("Return") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        } ?: throw IllegalStateException("Null activity")
    }
}