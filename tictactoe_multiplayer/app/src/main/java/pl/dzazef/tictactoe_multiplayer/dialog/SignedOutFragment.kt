package pl.dzazef.tictactoe_multiplayer.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

//USAGE: SignedOutFragment().show(supportFragmentManager, "dialog")
class SignedOutFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("Signed out")
                .setNeutralButton("Return") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        } ?: throw IllegalStateException("Null activity")
    }
}