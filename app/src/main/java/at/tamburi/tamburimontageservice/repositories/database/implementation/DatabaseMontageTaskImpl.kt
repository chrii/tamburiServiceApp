package at.tamburi.tamburimontageservice.repositories.database.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.LockerDao
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import at.tamburi.tamburimontageservice.services.database.dao.RemoteLocationDao
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "MontageTaskImpl"

class DatabaseMontageTaskImpl(
    private val montageTaskDao: MontageTaskDao,
    private val ownerDao: LocationOwnerDao,
    private val lockerDao: LockerDao,
    private val locationDao: RemoteLocationDao
) : IDatabaseMontageTaskRepository {
    override suspend fun getTaskById(id: Int): DataState<MontageTask> {
        return DataState(hasData = false, data = null, message = "getTaskById")
    }

    override suspend fun saveTasks(tasks: List<MontageTask>): Boolean {
        try {
            val tasksToEntity = tasks.map {
                montageTaskDao.saveTask(
                    montageTaskId = it.montageTaskId,
                    creationDate = it.creationDate.time,
                    locationId = it.location.locationId,
                    ownerId = it.locationOwner?.buildingOwnerId ?: -1,
                    montageStatus = it.montageStatus,
                    locationDescription = it.locationDescription,
                    powerConnection = it.powerConnection,
                    montageGroundName = it.montageGroundName,
                    montageSketchUrl = it.montageSketchUrl ?: "",
                    montageHint = it.montageHint,
                    lockerList = Locker.lockerIdToString(it.lockerList),
                    servicemanList = ServiceUser.toIdList(it.servicemanList),
                    scheduledInstallationDate = it.scheduledInstallationDate.time,
                )
            }
            Log.d(TAG, "TASKSTOENTITY: ${tasksToEntity.contains(-1)}")
            return tasksToEntity.contains(-1)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Saving Tasks was not successful - Stacktrace")
        }
    }

    override suspend fun setQrCode(qrCode: String, lockerId: Int): DataState<Boolean> {
        return try {
            lockerDao.setQrCode(qrCode, lockerId)
            DataState(
                hasData = true,
                data = true,
                message = "Success"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = false,
                message = "Error: Cant set QR Code for Locker"
            )
        }
    }

    override suspend fun getAllTasks(): DataState<List<MontageTask>> {
        return DataState(hasData = false, data = null, message = "getAllTasks")
    }
}