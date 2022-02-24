package at.tamburi.tamburimontageservice.services.database

import androidx.room.Database
import androidx.room.RoomDatabase
import at.tamburi.tamburimontageservice.services.database.dao.*
import at.tamburi.tamburimontageservice.services.database.entities.*


@Database(
    entities = [
        UserEntity::class,
        LocationOwnerEntity::class,
        MontageTaskEntity::class,
        LockerEntity::class,
        RemoteLocationEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ownerDao(): LocationOwnerDao
    abstract fun montageTaskDao(): MontageTaskDao
    abstract fun lockerDao(): LockerDao
    abstract fun remoteLocationDao(): RemoteLocationDao

}