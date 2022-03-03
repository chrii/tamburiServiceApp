package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lockers")
data class LockerEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "locker_id")
    val lockerId: Int,
    @NonNull
    @ColumnInfo(name = "location_id")
    val locationId: Int,
    @NonNull
    @ColumnInfo(name = "locker_name")
    val lockerName: String,
    @NonNull
    @ColumnInfo(name = "locker_type")
    val lockerType: Int,
    @NonNull
    @ColumnInfo(name = "column_number")
    val columnNumber: Int,
    @NonNull
    @ColumnInfo(name = "montage_task_id")
    val montageTaskId: Int,
    @NonNull
    @ColumnInfo(name = "type_name")
    val typeName: String,
    @NonNull
    @ColumnInfo(name = "gateway")
    val gateway: Boolean,
    @NonNull
    @ColumnInfo(name = "gateway_serial_number")
    val gatewaySerialnumber: String,
    @NonNull
    @ColumnInfo(name = "qr_code")
    val qrCode: String
)