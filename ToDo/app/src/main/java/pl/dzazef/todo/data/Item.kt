package pl.dzazef.todo.data

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

enum class Sort {
    DATE, PRIORITY, ICON
}

data class Item(val dateTime: String, val message: String, val color: Int, var priority: String, val icon: Int)

fun prioritySelector(i: Item): String = i.priority
fun iconSelector(i: Item): Int = i.icon

fun MutableList<Item>.sort(s: Sort) {
    when(s) {
        Sort.PRIORITY -> this.sortByDescending {prioritySelector(it)}
        Sort.DATE -> {
            this.sortWith(object: Comparator<Item> {
                override fun compare(o1: Item, o2: Item): Int {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm", Locale.US)
                    val d1 = LocalDateTime.parse(o1.dateTime, formatter)
                    val d2 = LocalDateTime.parse(o2.dateTime, formatter)
                    Log.d("INFO1", "$d1 $d2")
                    return when {
                        d1 > d2 -> 1
                        d1 < d2 -> -1
                        else -> 0
                    }
                }
            })
        }
        Sort.ICON -> this.sortByDescending { iconSelector(it) }
    }
}