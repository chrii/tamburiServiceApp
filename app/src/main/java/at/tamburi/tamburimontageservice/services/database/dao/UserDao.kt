package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_history ORDER BY loginDate LIMIT 1")
    suspend fun getUserData(): List<UserEntity>

    @Query("INSERT INTO user_history (username, userId, loginDate) VALUES (:username, :userId, :loginDate)")
    suspend fun saveUserEntry(username: String, userId: Int, loginDate: Long): Long
}