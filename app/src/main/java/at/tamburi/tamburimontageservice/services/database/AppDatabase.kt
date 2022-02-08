package at.tamburi.tamburimontageservice.services.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import at.tamburi.tamburimontageservice.services.database.dao.UserDao


abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}