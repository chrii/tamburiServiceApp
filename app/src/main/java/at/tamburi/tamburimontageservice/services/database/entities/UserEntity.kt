package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import at.tamburi.tamburimontageservice.models.ServiceUser

@Entity(tableName = "user_history")
data class UserEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "serviceman_id")
    val servicemanId: Int,

    @NonNull
    @ColumnInfo(name = "username")
    val username: String,

    @NonNull
    @ColumnInfo(name = "firstname")
    val firstname: String,

    @NonNull
    @ColumnInfo(name = "surname")
    val surname : String,

    @NonNull
    @ColumnInfo(name = "phone")
    val phone: String,

    @NonNull
    @ColumnInfo(name= "email")
    val email: String,

    @NonNull
    @ColumnInfo(name = "login_date")
    val loginDate: Long
)