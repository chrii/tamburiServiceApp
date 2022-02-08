package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import at.tamburi.tamburimontageservice.models.ServiceUser


val UserEntity.toServiceUser: ServiceUser
    get() = ServiceUser(
        userId = userId,
        username = username,
        loginDate = loginDate,
        magazineId = null,
        assignedMontageTaskId = null,
    )

@Entity(tableName = "user_history")
data class UserEntity(
    @PrimaryKey
    val id: Int,

    @NonNull
    @ColumnInfo(name = "username")
    val username: String,

    @NonNull
    @ColumnInfo(name = "userId")
    val userId: Int,

    @NonNull
    @ColumnInfo(name = "loginDate")
    val loginDate: Long
)