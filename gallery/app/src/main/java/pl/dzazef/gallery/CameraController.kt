package pl.dzazef.gallery

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
    private lateinit var currentPhotoPath : String

    /**
     * Permission request callback
     */
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
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
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = appCompatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            Log.d("DEBUG", "Created file: ${this.absolutePath}")
            currentPhotoPath = absolutePath
        }
    }

    /**
     * Method responsible for taking photos
     */
    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.d("DEBUG", "Error while creating file - aborting")
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
    fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            val f = File(currentPhotoPath)
            this.data = Uri.fromFile(f)
            appCompatActivity.addItemToRecyclerView(Item(this.data))
        }
    }

    /**
     * Method returning rotation of photo at given path
     */
    fun getRotation(path: String?): Float {
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
        if(!checkPermissions(appCompatActivity, PERMISSIONS))
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS, REQUEST_PERMISSIONS)
        else
            startCamera()
    }

    /**
     * Method checking if user has all needed permissions
     */
    private fun checkPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null) {
            for (p in permissions) {
                if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                    return false
            }
        }
        return true
    }
}
