package at.tamburi.tamburimontageservice.repositories.network

import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.utils.DataState

interface INetworkMontageTaskRepository {
    suspend fun getMontageTaskList(serviceUserId: Int): DataState<List<MontageTask>>
}