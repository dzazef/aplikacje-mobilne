package pl.dzazef.gallery.controllers

import android.content.Context
import android.support.v7.app.AppCompatActivity
import pl.dzazef.gallery.PREF_NAME
import pl.dzazef.gallery.PREF_DESCRIPTION
import pl.dzazef.gallery.PREF_RATING
import pl.dzazef.gallery.data.Item

class SharedPreferencesController(private val appCompatActivity: AppCompatActivity) {
    fun saveItemState(item: Item?) {
        if (item!=null) {
            val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            if (item.description != null) editor.putString(PREF_DESCRIPTION+item.path, item.description)
            if (item.rating != null) editor.putInt(PREF_RATING + item.path, item.rating!!)
            editor.apply()
        }
    }

    fun restoreItemState(item: Item) {
        val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        item.description = sharedPref.getString(PREF_DESCRIPTION+item.path, "test")
        val rating = sharedPref.getInt(PREF_DESCRIPTION+item.path, -1)
        item.rating = if (rating==-1) null else rating
    }

    fun saveDescription(path:String, description: String?) {
        if (description!=null) {
            val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_DESCRIPTION+path, description)
            editor.apply()
        }
    }

    fun saveRating(path: String, rating: Int?) {
        if (rating!=null) {
            val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt(PREF_DESCRIPTION+path, rating)
            editor.apply()
        }
    }
//
//    fun getDescription(path: String): String? {
//        val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//        return sharedPref.getString(PREF_DESCRIPTION+path, null)
//    }


    fun getRating(path: String): Int? {
        val sharedPref = appCompatActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val rating = sharedPref.getInt(PREF_RATING+path, -1)
        return if (rating==-1) null else rating
    }
}
