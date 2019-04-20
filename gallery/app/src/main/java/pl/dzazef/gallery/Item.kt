package pl.dzazef.gallery

import android.graphics.Bitmap

data class Item(val bitmap: Bitmap, val path: String)

fun pathSelector(i: Item): String = i.path


fun MutableList<Item>.sort() {
    this.sortByDescending { pathSelector(it) }
}