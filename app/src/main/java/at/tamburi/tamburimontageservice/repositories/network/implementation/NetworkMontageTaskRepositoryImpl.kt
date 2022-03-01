package at.tamburi.tamburimontageservice.repositories.network.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.services.network.services.INetworkMontageTaskService
import at.tamburi.tamburimontageservice.services.network.toMontageTask
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "NtwMontageTaskRepo"

class NetworkMontageTaskRepositoryImpl(private val networkMontageTaskService: INetworkMontageTaskService) :
    INetworkMontageTaskRepository {
    override suspend fun getMontageTaskList(serviceUserId: Int): DataState<List<MontageTask>> {
        return try {
            val result = networkMontageTaskService.getMontageTaskList(serviceUserId)
            if (result.isSuccessful) {
                Log.d(TAG, "Response is successful: ${result.isSuccessful}")
                val body = result.body() ?: throw Exception("getMontageTaskList - Body is empty")
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
                throw Exception("MontageTask request was not successful - getMontageTaskList")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Network Request Error")
        }
    }
}

