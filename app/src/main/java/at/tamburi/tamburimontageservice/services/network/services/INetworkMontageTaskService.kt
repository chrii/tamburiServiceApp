package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.LockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.MontageTaskDto
import retrofit2.Response
import retrofit2.http.*

interface INetworkMontageTaskService {
    @GET("remote_montage/mobile")
    suspend fun getMontageTaskList(
        @Query("servicemanId") serviceUserId: Int
    ): Response<List<MontageTaskDto>>

    @POST("remote_montage/mobile/register_locker")
    suspend fun registerLockers(
        @Body lockerList: List<LockerRegistrationDto>
    ): Response<Boolean>
}