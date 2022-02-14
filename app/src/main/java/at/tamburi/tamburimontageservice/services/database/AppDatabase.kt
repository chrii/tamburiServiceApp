package at.tamburi.tamburimontageservice.services.database

import androidx.room.Database
import androidx.room.RoomDatabase
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
import at.tamburi.tamburimontageservice.services.database.entities.LocationOwnerEntity
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity


@Database(
    entities = [
        UserEntity::class,
        LocationOwnerEntity::class,
        MontageTaskEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ownerDao(): LocationOwnerDao
    abstract fun montageTaskDao(): MontageTaskDao

}