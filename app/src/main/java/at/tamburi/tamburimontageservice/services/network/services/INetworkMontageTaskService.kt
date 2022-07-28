package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface INetworkMontageTaskService {
    @GET("remote_montage/mobile")
    suspend fun getMontageTaskList(
        @Query("servicemanId") serviceUserId: Int
    ): Response<List<MontageTaskDto>>

    @GET("claim")
    suspend fun getLocationClaims(
        @Query("locationId") locationId: Int,
        @Query("finalized") finalized: Boolean = false
    ): Response<List<ClaimDto>>

    @GET("claim/assigned_location")
    suspend fun getClaimLocations(
        @Query("servicemanId") serviceMan: Int
    ): Response<List<ClaimListObjectDto>>

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

    @POST("claim/defect_repaired/{claimId}")
    suspend fun confirmDefectRepaired(
    @Path("claimId") claimId: Int
    ): Response<Boolean>
}