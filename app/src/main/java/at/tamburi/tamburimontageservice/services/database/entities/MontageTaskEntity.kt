package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "montage_task")
data class MontageTaskEntity(
    @PrimaryKey
    @ColumnInfo(name="montage_task_id")
    val montageTaskId: Int,

    @NonNull
    @ColumnInfo(name = "creation_date")
    val creationDate: Long,

    @NonNull
    @ColumnInfo(name = "location_id")
    val locationId: Int,

    //NULL IS EMPTY
    @NonNull
    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    //TODO Maybe Pitfall? How to save an enum in kotlin.
    @NonNull
    @ColumnInfo(name = "montage_status")
    val montageStatus: String,

    @NonNull
    @ColumnInfo(name = "location_description")
    val locationDescription: String,

    @NonNull
    @ColumnInfo(name = "power_connection")
    val powerConnection: String,

    @NonNull
    @ColumnInfo(name = "montage_ground_name")
    val montageGroundName: String,

    @NonNull
    @ColumnInfo(name = "montage_sketch_url")
    val montageSketchUrl: String,

    @NonNull
    @ColumnInfo(name= "service_user_ids")
    val servicemanList: String,

    @NonNull
    @ColumnInfo(name= "montage_hint")
    val montageHint: String,

    @NonNull
    @ColumnInfo(name= "locker_list")
    val lockerList: String,

    @NonNull
    @ColumnInfo(name= "scheduled_installation_date")
    val scheduledInstallationDate: Long
)