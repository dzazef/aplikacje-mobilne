package pl.dzazef.gallery.camera

import android.media.ExifInterface
import android.util.Log

class Utils {
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
}