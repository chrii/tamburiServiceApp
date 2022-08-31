package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface INetworkMontageTaskService {
    @GET("remote_montage/mobile")
    suspend fun getMontageTaskList(
        @Query("servicemanId") serviceUserId: Int,
        @Header("Authorization") token: String
    ): Response<List<MontageTaskDto>>

    @GET("claim")
    suspend fun getLocationClaims(
        @Query("locationId") locationId: Int,
        @Query("finalized") finalized: Boolean = false,
        @Header("Authorization") token: String
    ): Response<List<ClaimDto>>

    @GET("claim/assigned_location")
    suspend fun getClaimLocations(
        @Query("servicemanId") serviceMan: Int,
        @Query("finalized") finalized: Boolean,
        @Header("Authorization") token: String
    ): Response<List<ClaimListObjectDto>>

    @POST("remote_montage/mobile/register_locker")
    suspend fun registerLockers(
        @Body lockerList: List<LockerRegistrationDto>,
        @Header("Authorization") token: String
    ): Response<Boolean>

    @POST("remote_montage/mobile/register_location")
    suspend fun registerLocation(
        @Body locationRegistrationObject: LocationRegistrationDto,
        @Header("Authorization") token: String
    ): Response<String>

    @POST("remote_montage/mobile/status")
    suspend fun setStatus(
        @Body status: StatusDto,
        @Header("Authorization") token: String
    ): Response<Boolean>

    @POST("claim/defect_repaired/{claimId}")
    suspend fun confirmDefectRepaired(
        @Path("claimId") claimId: Int,
        @Header("Authorization") token: String
    ): Response<Boolean>

    @POST("remote_montage/set_credentials/{locationId}")
    suspend fun getRegistrationQrCode(
        @Path("locationId") locationId: Int,
        @Header("Authorization") token: String
    ): Response<String>
}