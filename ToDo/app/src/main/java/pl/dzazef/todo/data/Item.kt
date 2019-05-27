package pl.dzazef.todo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "items"
)
data class Item(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @ColumnInfo(name = "date_time")
    val dateTime: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "priority")
    var priority: String,

    @ColumnInfo(name = "icon")
    val icon: Int
)
