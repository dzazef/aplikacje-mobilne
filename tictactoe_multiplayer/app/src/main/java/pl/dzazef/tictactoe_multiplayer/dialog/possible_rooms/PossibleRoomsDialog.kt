package pl.dzazef.tictactoe_multiplayer.dialog.possible_rooms

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.possible_rooms_dialog.*
import pl.dzazef.tictactoe_multiplayer.R
import pl.dzazef.tictactoe_multiplayer.firebase.db.RoomFinder
import pl.dzazef.tictactoe_multiplayer.game.GameActivity

class PossibleRoomsDialog(private val context : AppCompatActivity) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.possible_rooms_dialog)
        room_btn_new_room.setOnClickListener{ createNewRoom() }
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        room_rcl.layoutManager = LinearLayoutManager(context)
        val adapter = PossibleRoomAdapter(context, this, mutableListOf())
        room_rcl.adapter = adapter
        val manager = RoomFinder(context, adapter)
        manager.lookForRooms()
    }

    private fun createNewRoom() {
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra("NEW_ROOM", true)
        context.startActivity(intent)
        dismiss()
    }

    fun roomChosen(room: Room) {
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra("NEW_ROOM", false)
        intent.putExtra("ID", room.id)
        context.startActivity(intent)
        dismiss()
    }

    fun stopProgressBar() {
        room_progress.visibility = View.GONE
    }
}