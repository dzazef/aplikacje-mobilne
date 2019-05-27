package pl.dzazef.todo.db

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log

/**
 * Żeby otworzyć bazę danych do klasy MainActitvity dodajemy interfejs
 * OpenDatabase.OpenDatabaseListener i implementujemy metodę onDatabaseReady
 * Przykład:
 *  val open = OpenDatabase(this)
 *  open.setOpenDatabaseListener(this)
 *  open.load()
 */
class OpenDatabase(private val context: Context, private var listener : OpenDatabaseListener? = null) {
    interface OpenDatabaseListener {
        fun onDatabaseReady(db : AppDatabase)
        fun onDatabaseFail()
    }

    fun setOpenDatabaseListener(listener : OpenDatabaseListener) {
        this.listener = listener
    }

    fun load() {
        if (listener == null) {
            Log.e("DEBUG_ERROR", "ERROR: Listener not found")
            return
        } else {
            Thread{
                try {
                    val db: AppDatabase = Room
                        .databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "todo.db"
                        ).build()
                    listener?.onDatabaseReady(db)
                } catch (e: Exception) {
                    Log.e("ERROR_DATABASE", e.message, e)
                    listener?.onDatabaseFail()
                }
            }.start()
        }
    }
}
