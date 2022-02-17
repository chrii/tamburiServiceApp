package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "montage_task")
data class MontageTaskEntity(
    @PrimaryKey
    val id: Int,

    @NonNull
    @ColumnInfo(name = "montage_id")
    val montageId: Int,

    @NonNull
    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @NonNull
    @ColumnInfo(name = "remote_location_id")
    val remoteLocationId: Int,

    @NonNull
    @ColumnInfo(name = "magazine")
    val magazine: String,

    @NonNull
    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    @NonNull
    @ColumnInfo(name = "montage_status")
    val montageStatus: Int,

    @NonNull
    @ColumnInfo(name = "location_desc")
    val locationDesc: String,

    @NonNull
    @ColumnInfo(name = "power_connection")
    val powerConnection: Int,

    @NonNull
    @ColumnInfo(name = "montage_ground")
    val montageGround: String,

    @NonNull
    @ColumnInfo(name = "montage_sketch")
    val montageSketch: String,

    @NonNull
    @ColumnInfo(name = "locker_list")
    val lockerList: String,

    @NonNull
    @ColumnInfo(name = "assigned_monteurs")
    val assignedMonteurs: String,

    @NonNull
    @ColumnInfo(name = "scheduled_installation")
    val scheduledInstallation: Long,
)