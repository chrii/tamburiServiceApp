package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity

@Dao
interface MontageTaskDao {
    @Query("SELECT * FROM montage_task")
    suspend fun getAllTasks(): List<MontageTaskEntity>

    @Query("SELECT * FROM montage_task WHERE id = :montageId")
    suspend fun getTaskByTaskId(montageId: Int): MontageTaskEntity?

    @Query(
        "INSERT INTO montage_task (id, montage_id, created_at, remote_location_id, magazine, owner_id, montage_status, location_desc, power_connection, montage_ground, montage_sketch, locker_list, assigned_monteurs, scheduled_installation) " +
                "VALUES (:id, :montageId, :createdAt, :remoteLocationId, :magazine, :ownerId, :montageStatus, :locationDesc, :powerConnection, :montageGround, :montageSketch, :lockerList, :assignedMonteurs, :scheduledInstallation)"
    )
    suspend fun saveTask(
        id: Int,
        montageId: Int,
        createdAt: Long,
        remoteLocationId: Int,
        magazine: String,
        ownerId: Int,
        montageStatus: Int,
        locationDesc: String,
        powerConnection: Int,
        montageGround: String,
        montageSketch: String,
        lockerList: String,
        assignedMonteurs: String,
        scheduledInstallation: Long
    ): Long
}