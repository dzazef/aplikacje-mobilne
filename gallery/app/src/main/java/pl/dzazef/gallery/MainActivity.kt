package pl.dzazef.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

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
        Log.d("DEBUG", "Calling on create")
    }


    fun setRecyclerView() {
        recyclerView = main_rec
        recyclerView.layoutManager = GridLayoutManager(this, VERTICAL_SPAN_COUNT)
        this.adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }

    fun addItemToRecyclerView(item: Item) {
        Log.d("DEBUG", "Adding item to root_rcv")
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
            Log.d("DEBUG", "Setting Uri: ${item.file}")
            vh.itemImageView.setImageURI(item.file)
            vh.itemImageView.rotation = cameraController.getRotation(item.file!!.path)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            val itemImageView : ImageView = itemView.findViewById(R.id.item_imv)

            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraController.onRequestPermissionsResult(requestCode, grantResults)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onAddClick(view: View) {
        cameraController.onClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data!!.extras!!.get("uri") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_TAKE_PHOTO -> {
                    cameraController.galleryAddPic()
                }
            }

        }
    }

}
