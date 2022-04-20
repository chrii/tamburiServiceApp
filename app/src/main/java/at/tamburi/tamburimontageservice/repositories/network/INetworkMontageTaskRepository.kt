package at.tamburi.tamburimontageservice.repositories.network

import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.services.network.dto.LocationRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.LockerRegistrationDto
import at.tamburi.tamburimontageservice.utils.DataState

interface INetworkMontageTaskRepository {
    suspend fun getMontageTaskList(serviceUserId: Int): DataState<List<MontageTask>>
    suspend fun registerLockers(lockerList: List<Locker>): DataState<Boolean>
    suspend fun registerLocation(locationId: Int, qrCode: String): DataState<String>
    suspend fun setStatus(montageTaskId: Int, statusId: Int): DataState<Boolean>
}