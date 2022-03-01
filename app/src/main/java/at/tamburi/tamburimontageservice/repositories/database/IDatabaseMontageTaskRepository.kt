package at.tamburi.tamburimontageservice.repositories.database

import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.utils.DataState

interface IDatabaseMontageTaskRepository {
    suspend fun getAllTasks(): DataState<List<MontageTask>>
    suspend fun getTaskById(id: Int): DataState<MontageTask>
    suspend fun setQrCode(qrCode: String, lockerId: Int): DataState<Boolean>
    suspend fun saveTasks(tasks: List<MontageTask>): Boolean
}