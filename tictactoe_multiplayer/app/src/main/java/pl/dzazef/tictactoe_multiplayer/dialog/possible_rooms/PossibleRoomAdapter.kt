package pl.dzazef.tictactoe_multiplayer.dialog.possible_rooms

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.dzazef.tictactoe_multiplayer.R
import pl.dzazef.tictactoe_multiplayer.firebase.db.RoomFinder

class PossibleRoomAdapter(private val context : Context,
                          private val possibleRoomsDialog: PossibleRoomsDialog,
                          private val rooms : MutableList<Room>)
    : RecyclerView.Adapter<PossibleRoomAdapter.ViewHolder>(), RoomFinder.RoomManagerCallback {

    override fun roomFoundCallback(room: Room) {
        rooms.add(room)
        notifyDataSetChanged()
    }

    override fun allRoomFoundCallback() {
        possibleRoomsDialog.stopProgressBar()
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.room, p0, false))

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val room = rooms[p1]
        holder.id.text = room.id
        holder.user.text = room.user1
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val root : ConstraintLayout = itemView.findViewById(R.id.room_item_root)
        val id : TextView = itemView.findViewById(R.id.room_item_txt_id)
        val user : TextView = itemView.findViewById(R.id.room_item_txt_user)

        init {
            root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            possibleRoomsDialog.roomChosen(rooms[adapterPosition])
        }
    }
}