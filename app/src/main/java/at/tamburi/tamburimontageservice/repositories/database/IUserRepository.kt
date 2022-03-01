package at.tamburi.tamburimontageservice.repositories.database

import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.utils.DataState

interface IUserRepository {
    suspend fun getUser() : DataState<ServiceUser>
    suspend fun saveUser(user: ServiceUser): DataState<ServiceUser>
}