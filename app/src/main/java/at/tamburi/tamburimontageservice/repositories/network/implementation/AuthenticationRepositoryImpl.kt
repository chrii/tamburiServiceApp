package at.tamburi.tamburimontageservice.repositories.network.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.services.network.services.IAuthenticationService
import at.tamburi.tamburimontageservice.services.network.toServiceUser
import at.tamburi.tamburimontageservice.utils.DataState
import java.util.*

private const val TAG = "AuthRepoImpl"

class AuthenticationRepositoryImpl(
    private val service: IAuthenticationService
) : IAuthenticationRepository {
    var retryCount = 0
    override suspend fun getUser(username: String, password: String): DataState<ServiceUser> {
        return try {
            val result = service.getServiceUser(username, password)
            if (result.isSuccessful) {
                val date = Date().time
                val body = result.body() ?: throw Exception("Empty Body")
                Log.d(TAG, "Got Body: $body")
                when (result.code()) {
                    200 -> {
                        retryCount = 0
                        DataState(
                            hasData = true,
                            data = body.toServiceUser(date),
                            message = "Hello: ${body.name}"
                        )
                    }
                    500 -> {
                        if (retryCount <= 2) {
                            retryCount++
                            getUser(username, password)
                        }
                        DataState(
                            hasData = false,
                            data = null,
                            message = "No Connection to Server"
                        )
                    }
                    //TODO: Add common Network Errors
                    else -> DataState(
                        hasData = false,
                        data = null,
                        message = "Network Error Code: ${result.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "Connecting to Server was not Successful"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = null,
                message = e.message
            )
        }
    }
}