package at.tamburi.tamburimontageservice.modules

import android.content.Context
import androidx.room.Room
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseUserRepository
import at.tamburi.tamburimontageservice.repositories.database.implementation.DatabaseMontageTaskImpl
import at.tamburi.tamburimontageservice.repositories.database.implementation.DatabaseUserRepositoryImpl
import at.tamburi.tamburimontageservice.services.database.AppDatabase
import at.tamburi.tamburimontageservice.services.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context):
            AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun userDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideOwnerDao(appDatabase: AppDatabase): LocationOwnerDao = appDatabase.ownerDao()

    @Provides
    @Singleton
    fun provideLockerDao(appDatabase: AppDatabase): LockerDao = appDatabase.lockerDao()

    @Provides
    @Singleton
    fun provideRemoteLocationDao(appDatabase: AppDatabase): RemoteLocationDao =
        appDatabase.remoteLocationDao()

    @Provides
    @Singleton
    fun providesTaskRepositoryDao(appDatabase: AppDatabase) = appDatabase.montageTaskDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): IDatabaseUserRepository {
        return DatabaseUserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        montageTaskDao: MontageTaskDao,
        ownerDao: LocationOwnerDao,
        lockerDao: LockerDao,
        locationDao: RemoteLocationDao,
        userDao: UserDao
    ): IDatabaseMontageTaskRepository {
        return DatabaseMontageTaskImpl(montageTaskDao, ownerDao, lockerDao, locationDao, userDao)
    }
}