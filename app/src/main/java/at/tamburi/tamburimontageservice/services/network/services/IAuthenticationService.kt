package at.tamburi.tamburimontageservice.services.network.services

import at.tamburi.tamburimontageservice.services.network.dto.ServiceUserDto
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface IAuthenticationService {
    @POST("authenticate/serviceman")
    suspend fun getServiceUser(
        @Header("username") username: String,
        @Header("password") password: String
    ): Response<ServiceUserDto>
}