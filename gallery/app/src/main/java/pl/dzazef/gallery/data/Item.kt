package pl.dzazef.gallery.data

import android.graphics.Bitmap

data class Item(val bitmap: Bitmap, val path: String, var rating: Float, var description: String?) {

}

fun ratingSelector(i: Item): Float = i.rating

fun MutableList<Item>.sort() {
    this.sortByDescending { ratingSelector(it) }
}