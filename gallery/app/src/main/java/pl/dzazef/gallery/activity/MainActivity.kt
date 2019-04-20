package pl.dzazef.gallery.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import pl.dzazef.gallery.*
import pl.dzazef.gallery.camera.CameraController
import pl.dzazef.gallery.camera.Utils
import pl.dzazef.gallery.data.Item
import pl.dzazef.gallery.data.sort
import java.io.File

class MainActivity : AppCompatActivity() {
    var itemList : MutableList<Item> = mutableListOf()
    lateinit var inflater : LayoutInflater
    lateinit var recyclerView : RecyclerView
    lateinit var adapter : RecyclerViewAdapter
    lateinit var cameraController: CameraController
    val sharedPreferencesManager = SharedPreferencesManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG2", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        cameraController = CameraController(this, packageManager)
        reloadPhotos()
    }

    fun reloadPhotos() {
        Log.d("DEBUG2", "reloadPhotos")
        Thread{
            Log.d("DEBUG1", "Adding pictures from ${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")
            val files = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.listFiles()
            val threads = mutableListOf<Thread>()
            files?.forEach { file -> Thread { cameraController.galleryAddPic(file.absolutePath) }.also { threads.add(it) }.start() }
            threads.forEach{ it.join() }
            finishAddMultipleItemToRecyclerView()
        }.start()
    }

    fun addMultipleItemToRecyclerView(item: Item) {
        Log.d("DEBUG2", "addMultipleItemToRecyclerView")
        itemList.add(item)
        runOnUiThread { adapter.notifyItemInserted(itemList.size) }
    }

    fun finishAddMultipleItemToRecyclerView() {
        Log.d("DEBUG2", "finishAddMultipleItemToRecyclerView")
        itemList.sort()
        runOnUiThread { adapter.notifyDataSetChanged() }
    }

    fun setRecyclerView() {
        Log.d("DEBUG2", "setRecyclerView")
        recyclerView = main_rec
        recyclerView.layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this, LANDSCAPE_SPAN_COUNT)
            else -> GridLayoutManager(this, PORTRAIT_SPAN_COUNT)
        }
        this.adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            Log.d("DEBUG2", "onCreateViewHolder")
            val v : View = layoutInflater.inflate(R.layout.gallery_item, p0, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int {
            Log.d("DEBUG2", "getItemCount")
            return itemList.size
        }

        override fun onBindViewHolder(vh: ViewHolder, p: Int) {
            Log.d("DEBUG2", "onBindViewHolder")
            val item : Item = itemList[p]
            Log.d("DEBUG1", "Binding, path: ${item.path}")
            vh.itemImageView.setImageBitmap(item.bitmap)
            vh.itemImageView.rotation = Utils().getRotation(item.path)
            vh.cardView.tag = item.path
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            val itemImageView : ImageView = itemView.findViewById(R.id.item_imv)
            val cardView : CardView = itemView.findViewById(R.id.item_card)

            init {
                cardView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                callDetailActivity(itemList[this.adapterPosition])
            }
        }
    }

    fun callDetailActivity(item: Item) {
        Log.d("DEBUG2", "onItemClick")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(EXTRA_FILE_PATH, item.path)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d("DEBUG2", "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraController.onRequestPermissionsResult(requestCode, grantResults)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onAddClick(view: View) {
        Log.d("DEBUG2", "onAddClick")
        cameraController.onClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("DEBUG2", "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    REQUEST_TAKE_PHOTO -> {
                        cameraController.galleryAddPic(cameraController.currentPhotoPath)
                        finishAddMultipleItemToRecyclerView()
                    }
                }
            }
            RESULT_CANCELED -> {
                when (requestCode) {
                    REQUEST_TAKE_PHOTO -> {
                        val del = File(cameraController.currentPhotoPath).delete()
                        Log.d("DEBUG1", "Deleted bitmap? : $del")
                    }
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        Log.d("DEBUG2", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState?.putString("PATH", cameraController.currentPhotoPath)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.d("DEBUG2", "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        cameraController.currentPhotoPath = savedInstanceState!!.getString("PATH")!!
    }
}
