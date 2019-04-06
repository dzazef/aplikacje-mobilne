package pl.dzazef.todo.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import pl.dzazef.todo.R
import pl.dzazef.todo.data.Item
import pl.dzazef.todo.data.Sort
import pl.dzazef.todo.data.sort

const val MAX_PRIORITY = 9

class MainActivity : AppCompatActivity(), ActivityInterface {
    lateinit var itemList : MutableList<Item>
    private lateinit var inflater : LayoutInflater
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : RecyclerViewAdapter
    private var sortBy = Sort.PRIORITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(pl.dzazef.todo.R.layout.activity_main)

        recyclerView = main_rec
        inflater = layoutInflater

        recyclerView.setOnSwipeToDelete()
    }

    override fun addItemToView(item: Item) {
        itemList.add(item)
        itemList.sort(sortBy)
        adapter.notifyDataSetChanged()
    }


    override fun showDetailActivity(dateAndTime: String, message: String, color: Int, priority: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("DATE_TIME", dateAndTime)
        intent.putExtra("MESSAGE", message)
        intent.putExtra("COLOR", color)
        intent.putExtra("PRIORITY", priority)
        startActivity(intent)
    }

    override fun setRecyclerView() {
        this.itemList = mutableListOf()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        this.adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val v : View = layoutInflater.inflate(R.layout.data_record, p0, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val item : Item = itemList[position]
            viewHolder.container.setBackgroundColor(item.color)
            viewHolder.dateTime.text = item.dateTime
            viewHolder.message.text = item.message
            viewHolder.priority.text = item.priority
            viewHolder.icon.setImageResource(item.icon)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
            val dateTime : TextView = itemView.findViewById(R.id.root_txt_date)
            val message : TextView = itemView.findViewById(R.id.root_txt_message)
            val priority : TextView = itemView.findViewById(R.id.root_txt_priority)
            val container : ViewGroup = itemView.findViewById(R.id.root_data_record)
            val icon : ImageView = itemView.findViewById(R.id.root_imv_icon)
            private val viewPriority : View = itemView.findViewById(R.id.root_v_priority_click)

            init {
                this.container.setOnClickListener(this)
                this.viewPriority.setOnClickListener(this)
                this.viewPriority.setOnLongClickListener(this)
            }

            override fun onClick(v: View?) {
                if (v!!.id == container.id) {
                    val item = itemList[this.adapterPosition]
                    showDetailActivity(item.dateTime, item.message, item.color, item.priority)
                } else if (v.id == viewPriority.id) {
                    val item = itemList[this.adapterPosition]
                    item.priority = if (item.priority=="$MAX_PRIORITY") "1" else ((item.priority.toInt() + 1) % (MAX_PRIORITY+1)).toString()
                    itemList.sort(sortBy)
                    adapter.notifyDataSetChanged()
                    Log.d("INFO1", "test")
                }
            }

            override fun onLongClick(v: View?): Boolean {
                if (v!!.id == viewPriority.id) {
                    if (itemList[this.adapterPosition].priority == "1") {
                        itemList[this.adapterPosition].priority = "$MAX_PRIORITY"
                    } else {
                        itemList[this.adapterPosition].priority = "1"
                    }
                    itemList.sort(sortBy)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        }
    }

    fun addItemClick(view: View) {
        val intent = Intent(this, AddItemActivity::class.java)
        startActivityForResult(intent, 9001)
    }

    fun sortItemsClick(view: View) {
        sortBy = when (sortBy) {
            Sort.PRIORITY -> Sort.DATE
            Sort.DATE -> Sort.ICON
            Sort.ICON -> Sort.PRIORITY
        }
        itemList.sort(sortBy)
        Toast.makeText(this, "Sorting by ${sortBy.toString().toLowerCase()}", Toast.LENGTH_SHORT).show()
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001 && resultCode == Activity.RESULT_OK) {
            addItemToView(Item(
                dateTime = data!!.getStringExtra("DATE_TIME"),
                color = data.getIntExtra("COLOR", 0),
                message = data.getStringExtra("MESSAGE"),
                priority = data.getStringExtra("PRIORITY"),
                icon = data.getIntExtra("ICON", 0)
            ))
        }
    }

    private fun RecyclerView.setOnSwipeToDelete() {
        val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.adapterPosition
                itemList.removeAt(pos)
                adapter!!.notifyDataSetChanged()
            }
        }
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(this)
    }
}

