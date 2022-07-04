package at.tamburi.tamburimontageservice.repositories.network.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.Claim
import at.tamburi.tamburimontageservice.models.ServiceAssignment
import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.services.network.dto.LocationRegistrationDto
import at.tamburi.tamburimontageservice.services.network.dto.StatusDto
import at.tamburi.tamburimontageservice.services.network.services.INetworkMontageTaskService
import at.tamburi.tamburimontageservice.services.network.toClaim
import at.tamburi.tamburimontageservice.services.network.toLockerRegistrationDto
import at.tamburi.tamburimontageservice.services.network.toMontageTask
import at.tamburi.tamburimontageservice.services.network.toServiceAssignment
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "NtwMontageTaskRepo"

class NetworkMontageTaskRepositoryImpl(private val networkMontageTaskService: INetworkMontageTaskService) :
    INetworkMontageTaskRepository {
    var retry = 0

    override suspend fun getClaimLocations(servicemanId: Int): DataState<List<ServiceAssignment>> {
        return try {
            val response = networkMontageTaskService.getClaimLocations(servicemanId)
            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("Request: Response Body is null")
                Log.d(TAG, body.toString())
                when (response.code()) {
                    200 -> {
                        try {
                            val data = DataState(
                                hasData = true,
                                data = body.map { it.toServiceAssignment() })
                            Log.d(TAG, data.data.toString())
                            data
                        } catch (e: Exception) {
                            e.printStackTrace()
                            DataState(hasData = false, data = null, message = e.message)
                        }
                    }
                    else -> DataState(
                        hasData = false,
                        data = null,
                        message = "Request error code ${response.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "Request: Response is not successful - Code ${response.code()}"
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

    override suspend fun getLocationClaims(locationId: Int): DataState<List<Claim>> {
        return try {
            val response = networkMontageTaskService.getLocationClaims(locationId)

            if (response.isSuccessful) {
                val body = response.body() ?: throw Exception("Request Error: Body is null")
                when (response.code()) {
                    200 -> DataState(
                        hasData = true,
                        data = body.map { it.toClaim() }
                    )
                    else -> DataState(
                        hasData = false,
                        data = null,
                        message = "Response code is not okay - Code ${response.code()}"
                    )
                }
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "Response was not successful - Code ${response.code()}"
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
                        DataState(
                            hasData = true,
                            data = body,
                            message = "Request successful ${response.code()}"
                        )
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
            Log.d(TAG, "Response: $response")
            Log.d(TAG, "Response: ${response.isSuccessful}")
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${response.code()}")
                val secureBody =
                    response.body() ?: throw Exception("Error - Response Body is empty")
                when (response.code()) {
                    400 -> DataState(
                        hasData = false,
                        data = false,
                        message = "Gateway or Locker Serial in Use"
                    )
                    200 -> {
                        retry = 0
                        DataState(
                            hasData = true,
                            data = secureBody,
                            message = "Request Successful"
                        )
                    }
                    500 -> if (retry <= 2) {
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

