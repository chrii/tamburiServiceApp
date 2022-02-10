package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query

//@Dao
//interface MontageTaskDao {
//    @Query("SELECT * FROM montage_task")
//    suspend fun getAllTasks(): List<MontageTaskDao>
//
//    @Query("SELECT * FROM montage_task WHERE montage_id = :montageId")
//    suspend fun getTaskByTaskId(montageId: Int): MontageTaskDao?
//
//    @Query(
//        "INSERT INTO montage_task (montage_id, created_at, remoteLocation_id, magazine, owner_id, montage_status, location_desc, power_connection, montage_ground, montage_sketch, locker_count, locker_type_list, assigned_monteurs, scheduled_installation) " +
//                "VALUES (:montageId, :createdAt, :remoteLocationId, :magazine, :ownerId, :montageStatus, :locationDesc, :powerConnection, :montageGround, :lockerCount, :lockerTypeList, :assignedMonteurs, :scheduledInstallation)"
//    )
//    suspend fun saveTask(
//        montageId: Int,
//        createdAt: Long,
//        remoteLocationId: Int,
//        magazine: String,
//        ownerId: Int,
//        montageStatus: Int,
//        locationDesc: String,
//        powerConnection: Int,
//        montageGround: String,
//        lockerCount: Int,
//        lockerTypeList: String,
//        assignedMonteurs: String,
//        scheduledInstallation: Long
//    ): Long
//}