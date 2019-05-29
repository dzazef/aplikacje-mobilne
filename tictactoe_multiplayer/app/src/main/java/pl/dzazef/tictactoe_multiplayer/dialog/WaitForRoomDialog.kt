package pl.dzazef.tictactoe_multiplayer.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import pl.dzazef.tictactoe_multiplayer.R

class WaitForRoomDialog(context : Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.wait_for_room_dialog)
    }
}