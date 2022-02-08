package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity

interface UserDao {
    @Query("SELECT * FROM user_history ORDER BY loginDate LIMIT 1")
    suspend fun getUserData(): UserEntity
}