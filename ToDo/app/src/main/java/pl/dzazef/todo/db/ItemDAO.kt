package pl.dzazef.todo.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import pl.dzazef.todo.data.Item

@Dao
interface ItemDAO {

    @Query("SELECT * FROM items")
    fun getAll() : MutableList<Item>

    @Insert
    fun insertAll(vararg item : Item)

    @Insert
    fun insertAll(items : List<Item>)

    @Query("UPDATE items SET priority = :priority WHERE id = :id")
    fun updatePriority(id : Long, priority : String)

    @Delete
    fun deleteAll(vararg item : Item)

    @Delete
    fun deleteAll(items : List<Item>)
}