package at.tamburi.tamburimontageservice.repositories.network

import at.tamburi.tamburimontageservice.models.Claim
import at.tamburi.tamburimontageservice.models.ServiceAssignment
import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.utils.DataState

interface INetworkMontageTaskRepository {
    suspend fun getMontageTaskList(serviceUserId: Int, token: String): DataState<List<MontageTask>>
    suspend fun registerLockers(lockerList: List<Locker>, token: String): DataState<Boolean>
    suspend fun registerLocation(locationId: Int, qrCode: String, token: String): DataState<String>
    suspend fun setStatus(montageTaskId: Int, statusId: Int, token: String): DataState<Boolean>
    suspend fun getClaimLocations(servicemanId: Int, token: String): DataState<List<ServiceAssignment>>
    suspend fun getLocationClaims(locationId: Int, token: String): DataState<List<Claim>>
    suspend fun confirmDefectRepaired(claimId: Int, token: String): DataState<Boolean>
    suspend fun getRegistrationQrCode(locationId: Int, token: String): DataState<String>
}