package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.LocationRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.LockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.MontageTaskDto
import at.tamburi.tamburimontageservice.services.network.dto.StatusDto
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

    @POST("remote_montage/mobile/register_location")
    suspend fun registerLocation(
        @Body locationRegistrationObject: LocationRegistrationDto
    ): Response<String>

    @POST("remote_montage/mobile/status")
    suspend fun setStatus(
        @Body status: StatusDto
    ): Response<Boolean>
}