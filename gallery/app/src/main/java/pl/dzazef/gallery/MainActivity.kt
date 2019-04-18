package pl.dzazef.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_IMAGE_CAPTURE = 9001
private const val REQUEST_PERMISSIONS = 10001
private val PERMISSIONS = arrayOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.CAMERA
)

class MainActivity : AppCompatActivity() {
    var itemList : MutableList<Item> = mutableListOf()
    lateinit var inflater : LayoutInflater
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : RecyclerViewAdapter
    lateinit var cameraController: CameraController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        cameraController = CameraController(this, packageManager)
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
//        addItemToRecyclerView(Item())
    }

    fun setRecyclerView() {
        recyclerView = main_rec
        recyclerView.layoutManager = GridLayoutManager(this, 3)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraController.onRequestPermissionsResult(requestCode, grantResults)
    }

    fun onAddClick(view: View) {
        cameraController.onClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

}
