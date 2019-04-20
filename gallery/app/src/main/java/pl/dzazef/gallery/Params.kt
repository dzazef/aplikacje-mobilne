package pl.dzazef.gallery

const val REQUEST_IMAGE_CAPTURE = 9001
const val REQUEST_TAKE_PHOTO = 9002
const val REQUEST_PERMISSIONS = 10001
const val IMAGE_VIEW_DIMENSION = 250
val PERMISSIONS = arrayOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.CAMERA
)
const val VERTICAL_SPAN_COUNT = 3