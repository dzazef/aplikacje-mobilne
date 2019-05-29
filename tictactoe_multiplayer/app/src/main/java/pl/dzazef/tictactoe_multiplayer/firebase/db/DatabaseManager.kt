package pl.dzazef.tictactoe_multiplayer.firebase.db

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pl.dzazef.tictactoe_multiplayer.dialog.DatabaseErrorFragment

class DatabaseManager(private val context : AppCompatActivity) {

    private val db = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    fun lookForRooms() : List<Pair<String, String>> {
        val possibleRooms = mutableListOf<Pair<String, String>>()
        db.reference.child("room").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                DatabaseErrorFragment().show(context.supportFragmentManager, "dialog")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child("state").value.toString() == "waiting") {
                        Log.i("INFO1", "tu")
                        possibleRooms.add(Pair(ds.key!!, ds.child("user1").value.toString()))
                    }
                    Log.i("INFO1", ds.key)
                }
                Log.i("INFO1", possibleRooms.toString())
            }
        })
        return possibleRooms
    }

}