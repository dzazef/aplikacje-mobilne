package pl.dzazef.todo.db

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

//USAGE: DatabaseNotFoundDialogFragment().show(supportFragmentManager, "dialog")
class DatabaseNotFoundDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage("Database not found")
                .setNeutralButton("RETURN") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        } ?: throw IllegalStateException("Null activity")
    }
}