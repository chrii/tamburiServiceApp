package at.tamburi.tamburimontageservice.ui.ViewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import at.tamburi.tamburimontageservice.MainActivity
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.network.implementation.NetworkMontageTaskRepositoryImpl
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class State {
    Ready,
    Loading,
    Error
}

enum class QrCodeScannerState {
    Locker,
    Location,
    Gateway
}

private const val TAG = "MontageWorkflow"

@HiltViewModel
class MontageWorkflowViewModel
@Inject
constructor(
    private val databaseMontageTaskRepository: IDatabaseMontageTaskRepository,
    private val networkMontageTaskRepository: INetworkMontageTaskRepository
) : ViewModel() {
    private val _state: MutableState<State> = mutableStateOf(State.Loading)
    private val _task: MutableState<MontageTask?> = mutableStateOf(null)
    var qrCodeScannerState: QrCodeScannerState = QrCodeScannerState.Locker
    var activeLocker = _task.value?.lockerList?.first()
    var gatewaySerialnumber: MutableList<String> = mutableListOf()

    val state: MutableState<State> = _state
    val task: MutableState<MontageTask?> = _task
    val hasRegisteredGateway: Boolean = gatewaySerialnumber.isNotEmpty()

    fun changeState(s: State) {
        _state.value = s
    }

    private fun setQrCodeForLocker(
        lifecycle: Lifecycle,
        lockerId: Int,
        qrCode: String,
        navigation: NavController
    ) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            val result = databaseMontageTaskRepository.setLockerQrCode(qrCode, lockerId)
            if (result.hasData) {
                Log.v(TAG, "QR Code successfully added to database")
                changeState(State.Ready)
                navigation.navigate(R.id.action_qr_code_fragment_to_landing_fragment)
            } else {
                changeState(State.Error)
            }
        }
    }

    fun setLocationQrCode(lifecycle: Lifecycle, locationId: Int, qrCode: String, navigation: NavController) {
        navigation.navigate(R.id.action_qr_code_fragment_to_proposal_fragment)
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val result = databaseMontageTaskRepository.setLocationQrCode(locationId, qrCode)
                if(result.hasData) {
                    changeState(State.Ready)
                } else {
                    changeState(State.Error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Error)
            }
        }
    }

    fun setGatewayForLocker(
        lifecycle: Lifecycle,
        lockerId: Int,
        serialnumber: String,
        navigation: NavController? = null
    ) {
        // TODO: We need a QR Code validation for gateway QR codes
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            val result =
                databaseMontageTaskRepository.setGatewaySerialnumber(serialnumber, lockerId)
            if (result.hasData) {
                gatewaySerialnumber.add(serialnumber)
                qrCodeScannerState = QrCodeScannerState.Locker
                Log.d(TAG, "$gatewaySerialnumber")
                Log.d(TAG, "$hasRegisteredGateway")
                changeState(State.Ready)
                navigation?.navigate(R.id.action_qr_code_next_handler)
            } else {
                changeState(State.Error)
            }
        }
    }

    fun setDataForLocker(
        lifecycle: Lifecycle,
        lockerId: Int,
        serialnumber: String,
        qrCode: String,
        navigation: NavController
    ) {
        setGatewayForLocker(
            lifecycle,
            lockerId,
            gatewaySerialnumber.first()
        )
        setQrCodeForLocker(
            lifecycle,
            lockerId,
            qrCode,
            navigation
        )
    }

    fun setBusSlotForLocker(
        busSlot: Int,
        lockerId: Int,
        lifecycle: Lifecycle
    ) {
        lifecycle.coroutineScope.launch {
            databaseMontageTaskRepository.setBusSlot(lockerId, busSlot)
        }
    }

    fun getTask(context: Context, lifecycle: Lifecycle) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            delay(1000)
            val id = context.dataStore.data.map {
                it[DataStoreConstants.ACTIVE_TASK_ID] ?: -1
            }.first()
            if (id != -1) {
                val result = databaseMontageTaskRepository.getTaskById(id)
                if (result.hasData) {
                    _task.value = result.data!!
                    changeState(State.Ready)
                } else {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    Toast.makeText(context, "No assigned Task available", Toast.LENGTH_LONG).show()
                }
            } else {
                changeState(State.Error)
            }
        }
    }

    fun revokeTask(context: Context, lifecycle: Lifecycle) {
        lifecycle.coroutineScope.launch {
            context.dataStore.edit {
                it[DataStoreConstants.ACTIVE_TASK_ID] = -1
                it[DataStoreConstants.HAS_ACTIVE_TASK] = false
            }
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun hasEmptyQrCode(): Boolean {
        if (_task.value != null) {
            val qrCodes = _task.value!!.lockerList.map { it.qrCode.isEmpty() }
            return qrCodes.contains(true)
        }
        return true
    }

    fun registerLockers(lifecycle: Lifecycle, context: Context, navigation: NavController) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val locationId = task.value?.location?.locationId
                    ?: throw Exception("registerLockers - Location ID not found")
                val lockers = databaseMontageTaskRepository.getLockersByLocationId(locationId)
                if (lockers.hasData) {
                    val networkResponse =
                        networkMontageTaskRepository.registerLockers(lockers.data!!)
                    if (networkResponse.hasData) {
                        changeState(State.Ready)
                        navigation.navigate(R.id.action_landing_fragment_to_proposal_fragment)
                    } else {
                        Toast.makeText(context, networkResponse.message, Toast.LENGTH_SHORT).show()
                        changeState(State.Error)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Error)
            }
        }
    }
}