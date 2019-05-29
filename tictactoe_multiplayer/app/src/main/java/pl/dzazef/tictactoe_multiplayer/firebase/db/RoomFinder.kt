package pl.dzazef.tictactoe_multiplayer.firebase.db

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pl.dzazef.tictactoe_multiplayer.dialog.DatabaseErrorFragment
import pl.dzazef.tictactoe_multiplayer.dialog.possible_rooms.Room

class RoomFinder(private val context : AppCompatActivity, private var callback : RoomManagerCallback? = null) {

    interface RoomManagerCallback {
        fun roomFoundCallback(room : Room)
        fun allRoomFoundCallback()
    }

    private val db = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser!!

    fun lookForRooms() {
        db.reference.child("room").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                DatabaseErrorFragment().show(context.supportFragmentManager, "dialog")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child("state").value.toString() == "waiting" && ds.child("user1").value.toString() != user.email) {
                        callback?.roomFoundCallback(Room(ds.key!!, ds.child("user1").value.toString()))
                    }
                }
                callback?.allRoomFoundCallback()
            }
        })
    }

}