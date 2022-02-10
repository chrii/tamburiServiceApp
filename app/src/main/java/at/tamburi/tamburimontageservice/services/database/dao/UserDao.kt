package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_history ORDER BY login_date LIMIT 1")
    suspend fun getUserData(): List<UserEntity>

    @Query("INSERT INTO user_history (username, user_id, login_date, assigned_task) VALUES (:username, :userId, :loginDate, :assignedTask)")
    suspend fun saveUserEntry(username: String, userId: Int, loginDate: Long, assignedTask: Int): Long
}