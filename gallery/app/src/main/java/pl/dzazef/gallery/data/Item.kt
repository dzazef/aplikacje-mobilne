package pl.dzazef.gallery.data

import android.graphics.Bitmap
import android.util.Log

data class Item(val bitmap: Bitmap, val path: String, var rating: Float, var description: String?) {

}

fun ratingSelector(i: Item): Float = i.rating

fun MutableList<Item>.sort() {
    Log.d("DEBUG2", "sort")
    this.sortByDescending { ratingSelector(it) }
}