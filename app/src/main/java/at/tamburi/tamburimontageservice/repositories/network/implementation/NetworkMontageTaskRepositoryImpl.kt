package at.tamburi.tamburimontageservice.repositories.network.implementation

import android.location.Location
import android.util.Log
import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.services.network.dto.LocationRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.LockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.StatusDto
import at.tamburi.tamburimontageservice.services.network.services.INetworkMontageTaskService
import at.tamburi.tamburimontageservice.services.network.toLockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.toMontageTask
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "NtwMontageTaskRepo"

class NetworkMontageTaskRepositoryImpl(private val networkMontageTaskService: INetworkMontageTaskService) :
    INetworkMontageTaskRepository {
    var retry = 0

    override suspend fun getMontageTaskList(serviceUserId: Int): DataState<List<MontageTask>> {
        return try {
            Log.d(TAG, "Getting Tasklist for user: $serviceUserId")
            val result = networkMontageTaskService.getMontageTaskList(serviceUserId)
            if (result.isSuccessful) {
                Log.d(TAG, "Response is successful: ${result.isSuccessful}")
                val body = result.body() ?: throw Exception("getMontageTaskList - Body is empty")
                when (result.code()) {
                    200 -> {
                        retry = 0
                        DataState(
                            hasData = true,
                            data = body.map { it.toMontageTask() },
                            message = "Successful"
                        )
                    }
                    500 -> {
                        if (retry <= 2) {
                            retry++
                            getMontageTaskList(serviceUserId)
                        } else {
                            DataState(false, null, "500 Server Error")
                        }
                    }
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

    override suspend fun setStatus(montageTaskId: Int, statusId: Int): DataState<Boolean> {
        val statusDto = StatusDto(montageTaskId, statusId)
        return try {
            val response = networkMontageTaskService.setStatus(statusDto)
            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("setStatus: Body not found")
                when (response.code()) {
                    200 -> {
                        retry = 0
                        DataState(hasData = true, data = true, message = "Successful")
                    }
                    400 -> DataState(hasData = false, data = false, message = body.toString())
                    500 -> {
                        if (retry <= 2) {
                            retry++
                            setStatus(montageTaskId, statusId)
                        } else {
                            DataState(
                                hasData = false,
                                data = false,
                                message = "Internal Server Error"
                            )
                        }
                    }
                    else -> DataState(
                        hasData = false,
                        data = false,
                        message = "Error - Code: ${response.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = false,
                    message = "Request Server Information was not successful"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = false,
                message = "Error retrieving Data: ${e.message}"
            )
        }
    }

    override suspend fun registerLocation(locationId: Int, qrCode: String): DataState<String> {
        val locationRegistrationObject = LocationRegistrationDto(locationId, qrCode)
        return try {
            val response = networkMontageTaskService.registerLocation(locationRegistrationObject)
            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("Body is null")
                when (response.code()) {
                    200 -> {
                        retry = 0
                        DataState(hasData = true, data = body, message = "Request successful")
                    }
                    500 -> if (retry <= 2) {
                        retry++
                        registerLocation(locationId, qrCode)
                    } else {
                        DataState(
                            hasData = false,
                            data = null,
                            message = "Server not available - Code: ${response.code()}"
                        )
                    }
                    else -> DataState(
                        hasData = false,
                        data = null,
                        message = "Error - Code: ${response.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "Network Request was not successful - Code: ${response.code()}"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(hasData = false, data = null, message = "Network Request Error")
        }
    }

    override suspend fun registerLockers(lockerList: List<Locker>): DataState<Boolean> {
        val lockerRegistrationList = lockerList.map { it.toLockerRegistrationDto() }
        return try {
            val response = networkMontageTaskService.registerLockers(lockerRegistrationList)
            if (response.isSuccessful) {
                val secureBody =
                    response.body() ?: throw Exception("Error - Response Body is empty")
                when (response.code()) {
                    200 -> {
                        retry = 0
                        DataState(
                            hasData = true,
                            data = secureBody,
                            message = "Request Successful"
                        )
                    }
                    500 -> if(retry <= 2) {
                        retry++
                        registerLockers(lockerList)
                    } else DataState(
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
                Log.d(TAG, response.message())
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

