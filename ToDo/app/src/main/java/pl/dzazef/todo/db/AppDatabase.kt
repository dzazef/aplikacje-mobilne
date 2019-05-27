package pl.dzazef.todo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import pl.dzazef.todo.data.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDAO() : ItemDAO
}