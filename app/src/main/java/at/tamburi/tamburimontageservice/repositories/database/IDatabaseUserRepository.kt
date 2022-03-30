package at.tamburi.tamburimontageservice.repositories.database

import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.utils.DataState

interface IDatabaseUserRepository {
    suspend fun getUser(userId: Int) : DataState<ServiceUser>
    suspend fun saveUser(user: ServiceUser): DataState<ServiceUser>
}