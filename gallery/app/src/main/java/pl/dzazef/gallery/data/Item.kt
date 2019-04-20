package pl.dzazef.gallery.data

import android.graphics.Bitmap

data class Item(val bitmap: Bitmap, val path: String, var rating: Int?, var description: String?) {

}

fun pathSelector(i: Item): String = i.path


fun MutableList<Item>.sort() {
    this.sortByDescending { pathSelector(it) }
}