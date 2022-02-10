package at.tamburi.tamburimontageservice.modules

import android.content.Context
import androidx.room.Room
import at.tamburi.tamburimontageservice.repositories.IUserRepository
import at.tamburi.tamburimontageservice.repositories.implementation.UserRepositoryImpl
import at.tamburi.tamburimontageservice.services.database.AppDatabase
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
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
    fun provideUserRepository(userDao: UserDao): IUserRepository {
        return UserRepositoryImpl(userDao)
    }
}