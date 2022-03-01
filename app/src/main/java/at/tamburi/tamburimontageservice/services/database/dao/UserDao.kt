package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_history ORDER BY login_date DESC LIMIT 1")
    suspend fun getLastLogin(): List<UserEntity>

    @Query("SELECT * FROM user_history WHERE serviceman_id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("INSERT INTO user_history (serviceman_id, username, firstname, surname, phone, email, login_date) VALUES (:servicemanId,:username,:firstname,:surname,:phone,:email,:loginDate)")
    suspend fun saveUserEntry(
        servicemanId: Int,
        username: String,
        firstname: String,
        surname: String,
        phone: String,
        email: String,
        loginDate: Long
    ): Long

    @Query("UPDATE user_history SET login_date = :date WHERE serviceman_id = :id")
    suspend fun updateDate(id: Int, date: Long): Int
}