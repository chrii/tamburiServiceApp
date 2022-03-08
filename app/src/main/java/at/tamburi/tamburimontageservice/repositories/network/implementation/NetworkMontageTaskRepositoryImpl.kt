package at.tamburi.tamburimontageservice.repositories.network.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.services.network.dto.LockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.services.INetworkMontageTaskService
import at.tamburi.tamburimontageservice.services.network.toLockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.toMontageTask
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "NtwMontageTaskRepo"

class NetworkMontageTaskRepositoryImpl(private val networkMontageTaskService: INetworkMontageTaskService) :
    INetworkMontageTaskRepository {
    override suspend fun getMontageTaskList(serviceUserId: Int): DataState<List<MontageTask>> {
        return try {
            Log.d(TAG, "Getting Tasklist for user: $serviceUserId")
            val result = networkMontageTaskService.getMontageTaskList(serviceUserId)
            if (result.isSuccessful) {
                Log.d(TAG, "Response is successful: ${result.isSuccessful}")
                val body = result.body() ?: throw Exception("getMontageTaskList - Body is empty")
                Log.d(TAG, "Response Body: $body")
                when (result.code()) {
                    200 -> DataState(
                        hasData = true,
                        data = body.map { it.toMontageTask() },
                        message = "Successful"
                    )
                    500 -> DataState(false, null, "500 Server Error")
                    else -> DataState(false, null, "Network Error - ${result.code()}")
                }
            } else {
                throw Exception("MontageTask request was not successful - Error Code: ${result.code()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Network Request Error")
        }
    }

    override suspend fun registerLockers(lockerList: List<Locker>): DataState<Boolean> {
        val lockerRegistrationList = lockerList.map { it.toLockerRegistrationDto() }
        Log.d(TAG, "LockerRegistrationList: $lockerRegistrationList")
        return try {
            val response = networkMontageTaskService.registerLockers(lockerRegistrationList)
            if (response.isSuccessful) {
                val secureBody =
                    response.body() ?: throw Exception("Error - Response Body is empty")
                when (response.code()) {
                    200 -> DataState(
                        hasData = true,
                        data = secureBody,
                        message = "Request Successful"
                    )
                    500 -> DataState(
                        hasData = false,
                        data = false,
                        message = "Registration Lockers - Server Error"
                    )
                    else -> DataState(
                        hasData = false,
                        data = false,
                        message = "Error Locker Registration - Errorcode: ${response.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = false,
                    message = "Locker Registration was not successful"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = false,
                message = "Error sending Locker Registration Request"
            )
        }
    }
}

