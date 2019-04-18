package pl.dzazef.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var itemList : MutableList<Item>
    lateinit var inflater : LayoutInflater
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        addItemToRecyclerView(Item())
    }

    fun setRecyclerView() {
        recyclerView = main_rec
        this.itemList = mutableListOf()
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        this.adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }

    fun addItemToRecyclerView(item: Item) {
        itemList.add(item)
        adapter.notifyDataSetChanged()
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val v : View = layoutInflater.inflate(R.layout.gallery_item, p0, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(vh: ViewHolder, p: Int) {
            val item : Item = itemList[p]
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }
}
