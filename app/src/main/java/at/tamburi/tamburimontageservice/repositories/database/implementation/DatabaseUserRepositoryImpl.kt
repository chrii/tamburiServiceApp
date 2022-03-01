package at.tamburi.tamburimontageservice.repositories.database.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseUserRepository
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
import at.tamburi.tamburimontageservice.services.database.toServiceUser
import at.tamburi.tamburimontageservice.utils.DataState
import java.util.*

private const val TAG = "UserRepositoryImpl"

class DatabaseUserRepositoryImpl(
    private val userDao: UserDao
) : IDatabaseUserRepository {
    override suspend fun getUser(): DataState<ServiceUser> {
        try {
            val result = userDao.getLastLogin()
            return if (!result.isNullOrEmpty()) {
                DataState(
                    hasData = true,
                    data = result.first().toServiceUser,
                    message = "Got User"
                )
            } else {
                DataState(
                    hasData = false,
                    message = "No user found",
                )
            }
        } catch (e: Exception) {
            e.stackTrace
            throw Exception("Error getting user data - ${e.message}")
        }
    }

    override suspend fun saveUser(user: ServiceUser): DataState<ServiceUser> {
        return try {
            val dBuser = userDao.getUserById(user.servicemanId)
            if (dBuser == null) {
                val userState = userDao.saveUserEntry(
                    user.servicemanId,
                    user.username,
                    user.firstname,
                    user.surname,
                    user.phone,
                    user.email,
                    user.loginDate
                )
                Log.d(TAG, "$userState")
                if (userState < 1L) {
                    DataState(hasData = false, data = null, message = "Couldn't save ServiceUser")
                } else {
                    DataState(hasData = true, data = user)
                }
            } else {
                val changedDate = userDao.updateDate(user.servicemanId, Date().time)
                Log.d(TAG, "Changed Date State after SQL: $changedDate")
                if (changedDate <= 0) {
                    DataState(
                        hasData = false,
                        data = null,
                        message = "Couldn't update ServiceUsers Login Date"
                    )
                } else {
                    DataState(hasData = true, data = user)
                }
            }
        } catch (e: Exception) {
            DataState(hasData = false, message = e.localizedMessage)
        }
    }
}