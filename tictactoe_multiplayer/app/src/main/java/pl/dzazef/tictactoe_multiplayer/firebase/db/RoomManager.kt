package pl.dzazef.tictactoe_multiplayer.firebase.db

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pl.dzazef.tictactoe_multiplayer.dialog.DatabaseErrorFragment
import pl.dzazef.tictactoe_multiplayer.dialog.WaitForRoomDialog
import pl.dzazef.tictactoe_multiplayer.dialog.WaitForUserDialog
import java.util.*

class RoomManager(private val context : AppCompatActivity, private val callback : RoomManagerCallback) : ValueEventListener {
    override fun onCancelled(p0: DatabaseError) {
        DatabaseErrorFragment().show(context.supportFragmentManager, "dialog")
    }

    override fun onDataChange(p0: DataSnapshot) {
        if(p0.child("round").value.toString() == myUser) {
            callback.setRound(true)
        } else {
            callback.setRound(false)
        }
        callback.setScore(p0.child("score").value.toString())
    }

    interface RoomManagerCallback {
        fun setPlaying(playing : Boolean)
        fun setRound(round : Boolean)
        fun setScore(score: String)
        fun setUser(user : String)
    }

    private val db = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser!!
    private lateinit var ref : DatabaseReference
    private lateinit var myUser : String

    private fun createEmptyRoom() {
        db.reference.child("room").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                DatabaseErrorFragment().show(context.supportFragmentManager, "dialog")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dialog = WaitForRoomDialog(context).also { it.show() }
                val roomList = mutableListOf<String>()
                for (ds in dataSnapshot.children) {
                    roomList.add(ds.key!!)
                }
                connectToNewRoom(getFreeId(roomList), dialog)
            }

            fun getFreeId(roomList : List<String>): String {
                val random = Random()
                var id = random.nextInt(1000)
                while(roomList.contains(id.toString())) {
                   id = random.nextInt(1000)
                }
                return id.toString()
            }
        })
    }

    private fun connectToNewRoom(id : String, dialog : Dialog) {
        myUser = "user1"
        callback.setUser(myUser)
        ref = db.getReference("room/$id")
        ref.addValueEventListener(this@RoomManager)
        ref.child(myUser).setValue(user.email)
        ref.child("state").setValue("waiting")
        ref.child("round").setValue("user1")
        ref.child("score").setValue("UUUUUUUUU")
        dialog.dismiss()
        val waitForUserDialog = WaitForUserDialog(context)
        waitForUserDialog.show()
        ref.child("state").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.e("ERROR1", "RoomManager.connectToNewRoom.onCancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value.toString() == "playing") {
                    ref.child("state").removeEventListener(this)
                    callback.setPlaying(true)
                    callback.setRound(true)
                    waitForUserDialog.dismiss()
                }
            }
        })

    }

    private fun connectToExistingRoom(id : String) {
        myUser = "user2"
        callback.setUser(myUser)
        val dialog = WaitForRoomDialog(context).also { it.show() }
        ref = db.getReference("room/$id")
        ref.child(myUser).setValue(user.email)
        ref.child("state").setValue("playing")
        callback.setPlaying(true)
        callback.setRound(false)
        ref.addValueEventListener(this@RoomManager)
        dialog.dismiss()
    }

    fun connect(id : String?) {
        if (id == null) {
            createEmptyRoom()
        } else {
            connectToExistingRoom(id)
        }
    }

    fun sendScore(score: String) {
        ref.child("score").setValue(score)
        ref.child("round").setValue(if (myUser == "user1") "user2" else "user1")
    }

    fun removeRoom() {
        ref.removeValue()
    }
}