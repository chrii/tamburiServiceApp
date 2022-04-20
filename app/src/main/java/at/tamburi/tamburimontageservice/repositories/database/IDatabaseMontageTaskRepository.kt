package at.tamburi.tamburimontageservice.repositories.database

import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.utils.DataState

interface IDatabaseMontageTaskRepository {
    suspend fun getAllTasks(): DataState<List<MontageTask>>
    suspend fun getTaskById(id: Int): DataState<MontageTask>
    suspend fun getLockersByTaskId(montageTaskId: Int): DataState<List<Locker>>
    suspend fun setStatus(taskId: Int, status: Int): DataState<Boolean>
    suspend fun setBusSlot(lockerId: Int, busSlot: Int): DataState<Boolean>
    suspend fun setLockerQrCode(qrCode: String, lockerId: Int): DataState<Boolean>
    suspend fun setLocationQrCode(locationId: Int, qrCode: String): DataState<Boolean>
    suspend fun setGatewaySerialnumber(serialnumber: String, lockerId: Int): DataState<Boolean>
    suspend fun setGPSCoordinates(lon: Double, lat: Double, locationId: Int): DataState<Boolean>
    suspend fun saveTasks(tasks: List<MontageTask>): DataState<List<MontageTask>>
}