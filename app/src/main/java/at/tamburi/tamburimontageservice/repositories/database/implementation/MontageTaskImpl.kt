package at.tamburi.tamburimontageservice.repositories.database.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.database.IMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.LockerDao
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import at.tamburi.tamburimontageservice.services.database.dao.RemoteLocationDao
//import at.tamburi.tamburimontageservice.services.database.toLocationOwner
//import at.tamburi.tamburimontageservice.services.database.toRemoteLocation
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "MontageTaskImpl"

class MontageTaskImpl(
    private val montageTaskDao: MontageTaskDao,
    private val ownerDao: LocationOwnerDao,
    private val lockerDao: LockerDao,
    private val locationDao: RemoteLocationDao
) : IMontageTaskRepository {
    override suspend fun getTaskById(id: Int): DataState<MontageTask> {
        return DataState(hasData = false, data = null, message = "getTaskById")
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

    override suspend fun saveMockMontageTask(task: MontageTask) {
        //TODO: Implement later
    }
}