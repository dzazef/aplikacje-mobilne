package pl.dzazef.gallery

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraController(private val appCompatActivity: MainActivity, private val packageManager: PackageManager) {
    lateinit var currentPhotoPath : String

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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = appCompatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            Log.d("INFO", "Created file: ${this.absolutePath}")
            currentPhotoPath = absolutePath
        }
    }

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.d("INFO", "Error while creating file - aborting")
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

    fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            val f = File(currentPhotoPath)
            this.data = Uri.fromFile(f)
            appCompatActivity.addItemToRecyclerView(Item(this.data))
        }
    }


    fun onClick() {
        if(!checkPermissions(appCompatActivity, PERMISSIONS))
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS, REQUEST_PERMISSIONS)
        else
            startCamera()
    }

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
