package pl.dzazef.gallery

//REQUEST CODES
const val REQUEST_TAKE_PHOTO = 9001
const val REQUEST_PERMISSIONS = 10001
//OTHER
const val IMAGE_VIEW_DIMENSION = 250
val PERMISSIONS = arrayOf(
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.CAMERA
)
const val PORTRAIT_SPAN_COUNT = 3
const val LANDSCAPE_SPAN_COUNT = 6
const val EXTRA_FILE_PATH = "FILE_PATH"