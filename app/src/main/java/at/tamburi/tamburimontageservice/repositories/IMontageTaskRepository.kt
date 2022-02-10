package at.tamburi.tamburimontageservice.repositories

import at.tamburi.tamburimontageservice.models.MontageTask

interface IMontageTaskRepository {
    suspend fun getAllTasks(): List<MontageTask>
    suspend fun saveMockMontageTask()

}