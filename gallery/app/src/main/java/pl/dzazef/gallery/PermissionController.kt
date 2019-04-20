package pl.dzazef.gallery

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

class PermissionController(private val appCompatActivity: AppCompatActivity, private val cameraController: CameraController) {

    /**
     * Method called on add photo click.
     * Method has to check if user has needed permissions.
     */
    fun onClick() {
        Log.d("DEBUG2", "onClick")
        if(!checkPermissions(appCompatActivity, PERMISSIONS))
            ActivityCompat.requestPermissions(appCompatActivity, PERMISSIONS, REQUEST_PERMISSIONS)
        else
            cameraController.startCamera()
    }

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
                    cameraController.startCamera()
                }
            }
            else -> Unit
        }
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