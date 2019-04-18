package pl.dzazef.gallery

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

private const val REQUEST_IMAGE_CAPTURE = 9001
private const val REQUEST_PERMISSIONS = 10001
private val PERMISSIONS = arrayOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.CAMERA
)
class CameraController(private val appCompatActivity: AppCompatActivity, private val packageManager: PackageManager) {

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

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                appCompatActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
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
