package at.tamburi.tamburimontageservice.repositories.network

import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.utils.DataState

interface IAuthenticationRepository {
    suspend fun getUser(username: String, password: String): DataState<ServiceUser>
}