package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity

@Dao
interface MontageTaskDao {
    @Query("SELECT * FROM montage_task")
    suspend fun getAllTasks(): List<MontageTaskEntity>

    @Query("SELECT * FROM montage_task WHERE montage_task_id = :montageId")
    suspend fun getTaskByTaskId(montageId: Int): MontageTaskEntity?

    @Query(
        "INSERT INTO montage_task (montage_task_id, creation_date, location_id, owner_id, status_id, location_description, power_connection, montage_ground_name, montage_sketch_url, locker_list, montage_hint, service_user_ids, scheduled_installation_date) " +
                "VALUES (:montageTaskId, :creationDate, :locationId, :ownerId, :statusId, :locationDescription, :powerConnection, :montageGroundName, :montageSketchUrl, :lockerList, :montageHint, :servicemanList, :scheduledInstallationDate)"
    )
    suspend fun saveTask(
        montageTaskId: Int,
        creationDate: Long,
        locationId: Int,
        ownerId: Int,
        statusId: Int,
        locationDescription: String,
        powerConnection: String,
        montageGroundName: String,
        montageSketchUrl: String,
        servicemanList: String,
        montageHint: String,
        lockerList: String,
        scheduledInstallationDate: Long
    ): Long

    @Query("UPDATE montage_task SET status_id = :status WHERE montage_task_id = :taskId")
    suspend fun setStatus(taskId: Int, status: Int): Int
}