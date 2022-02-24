package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lockers")
data class LockerEntity(
    @PrimaryKey
    @ColumnInfo(name= "locker_id")
    val lockerId: Int,

    @NonNull
    @ColumnInfo(name = "type_id")
    val typeId: Int,

    @NonNull
    @ColumnInfo(name = "type_name")
    val typeName: String,

    @NonNull
    @ColumnInfo(name = "qr_code")
    val qrCode: String,

    @NonNull
    @ColumnInfo(name = "gateway")
    val gateway: Boolean
)