package at.tamburi.tamburimontageservice.repositories

import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.utils.DataState

interface IMontageTaskRepository {
    suspend fun getAllTasks(): DataState<List<MontageTask>>
    suspend fun saveMockMontageTask(task: MontageTask)
    suspend fun getTaskById(id: Int): DataState<MontageTask>
}