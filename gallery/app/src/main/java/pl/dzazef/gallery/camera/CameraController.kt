package pl.dzazef.gallery.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import pl.dzazef.gallery.IMAGE_VIEW_DIMENSION
import pl.dzazef.gallery.controllers.PermissionController
import pl.dzazef.gallery.REQUEST_TAKE_PHOTO
import pl.dzazef.gallery.activity.MainActivity
import pl.dzazef.gallery.controllers.SharedPreferencesController
import pl.dzazef.gallery.data.Item
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class responsible for managing Camera interface and saving photos
 */
class CameraController(private val appCompatActivity: MainActivity, private val packageManager: PackageManager) {
    private val permissionController = PermissionController(appCompatActivity, this)
    var currentPhotoPath = ""


    /**
     * Method creating File for photo
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d("DEBUG2", "createImageFile")
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = appCompatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            Log.d("DEBUG1", "Created bitmap: ${this.absolutePath}")
            currentPhotoPath = absolutePath
        }
    }

    /**
     * Method responsible for taking photos
     */
    fun startCamera() {
        Log.d("DEBUG2", "startCamera")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.d("DEBUG1", "Error while creating file - aborting")
                    return
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        appCompatActivity,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    appCompatActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    fun getBitMap(path: String): Bitmap? {
        Log.d("DEBUG2", "galleryAddPic")
        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight
            val scaleFactor: Int = Math.min(photoW, photoH)/ IMAGE_VIEW_DIMENSION
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        return BitmapFactory.decodeFile(path, bmOptions)
    }

    /**
     * Method responsible for adding photo to recycler view(gallery)
     */
    fun galleryAddPic(path: String) {
        val bitmap = getBitMap(path)
        if (bitmap != null) {
            val item = Item(bitmap, path, null, null)
            SharedPreferencesController(appCompatActivity).restoreItemState(item)
            appCompatActivity.addMultipleItemToRecyclerView(item)
        }
    }




    fun onClick() = permissionController.onClick()
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) = permissionController.onRequestPermissionsResult(requestCode, grantResults)
}
