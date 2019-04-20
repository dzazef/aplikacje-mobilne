package pl.dzazef.gallery

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class responsible for managing Camera interface and saving photos
 */
class CameraController(private val appCompatActivity: MainActivity, private val packageManager: PackageManager) {
    var currentPhotoPath = ""

    /**
     * Permission request callback
     */
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        Log.d("DEBUG2", "onRequestPermissionsResult")
        when(requestCode) {
            REQUEST_PERMISSIONS -> {
                if (!(grantResults.isNotEmpty() && grantResults.all { it== PackageManager.PERMISSION_GRANTED } )) {
                    Toast.makeText(appCompatActivity, "You have to grant permissions to add photos", Toast.LENGTH_SHORT).show()
                } else {
                    startCamera()
                }
            }
            else -> Unit
        }
    }

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
    private fun startCamera() {
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

    /**
     * Method responsible for adding photo to recycler view(gallery)
     */
    fun galleryAddPic(path: String) {
            Log.d("DEBUG2", "galleryAddPic")
            val bmOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(path, this)
                val photoW: Int = outWidth
                val photoH: Int = outHeight
                val scaleFactor: Int = Math.min(photoW, photoH)/IMAGE_VIEW_DIMENSION
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
            }
            val bm = BitmapFactory.decodeFile(path, bmOptions)
            if (bm!=null) appCompatActivity.addItemToRecyclerView(Item(bm, path))
    }

    /**
     * Method returning rotation of photo at given path
     */
    fun getRotation(path: String?): Float {
        Log.d("DEBUG2", "getRotation")
        var rotate = 0f
        try {
            val exif: ExifInterface?
            exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            rotate = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                else -> 0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    /**
     * Method called on add photo click.
     * Method has to check if user has needed permissions.
     */
    fun onClick() {
        Log.d("DEBUG2", "onClick")
        if(!checkPermissions(appCompatActivity, PERMISSIONS))
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS, REQUEST_PERMISSIONS)
        else
            startCamera()
    }

    /**
     * Method checking if user has all needed permissions
     */
    private fun checkPermissions(context: Context?, permissions: Array<String>): Boolean {
        Log.d("DEBUG2", "checkPermissions")
        if (context != null) {
            for (p in permissions) {
                if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                    return false
            }
        }
        return true
    }
}
