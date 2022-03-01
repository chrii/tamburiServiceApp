package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.MontageTaskDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INetworkMontageTaskService {
    @GET("remote_montage/mobile")
    suspend fun getMontageTaskList(
        @Query("servicemanId") serviceUserId: Int
    ): Response<List<MontageTaskDto>>
}