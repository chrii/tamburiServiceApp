package at.tamburi.tamburimontageservice.repositories.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.IUserRepository
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
import at.tamburi.tamburimontageservice.services.database.toServiceUser
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl(
    private val userDao: UserDao
) : IUserRepository {
    override suspend fun getUser(): DataState<ServiceUser> {
        try {
            val result = userDao.getUserData()
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
            throw Exception("Error getting user data")
        }
    }

    override suspend fun saveUser(user: ServiceUser): DataState<ServiceUser> {
        return try {
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
        } catch (e: Exception) {
            DataState(hasData = false, message = e.localizedMessage)
        }
    }
}