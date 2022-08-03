package at.tamburi.tamburimontageservice.ui.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Criteria
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.Locker
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.ui.MainActivity.MainActivity
import at.tamburi.tamburimontageservice.utils.Constants
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.dataStore
import com.google.android.gms.location.LocationServices
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.stream.IntStream
import javax.inject.Inject

enum class State {
    Ready,
    Loading,
    Next,
    Error
}

enum class QrCodeScannerState {
    Locker,
    Location,
    Gateway
}

private const val TAG = "MontageWorkflow"

enum class WorkflowState {
    FINISHED,
    SCANNING
}

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
    var gatewaySerialnumberList: MutableList<String> = mutableListOf()
    var lockerQrCode: String? = null
    var registrationQrCode: String = ""
    var workflowState: MutableState<WorkflowState> = mutableStateOf(WorkflowState.SCANNING)

    val state: MutableState<State> = _state
    val task: MutableState<MontageTask?> = _task
    val hasRegisteredGateway: Boolean = gatewaySerialnumberList.isNotEmpty()

    fun changeState(s: State) {
        _state.value = s
    }

    fun changeWorkflowState(state: WorkflowState) {
        workflowState.value = state
    }

    fun checkWorkflowState(lifecycle: Lifecycle, context: Context) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            val datastore: String? = context.dataStore.data
                .map { it[DataStoreConstants.WORKFLOW_STATE] }
                .first()
            if (datastore.isNullOrEmpty()) {
                changeState(State.Ready)
            } else {
                if(datastore == "finished") {
                    changeState(State.Next)
                }
            }
        }
    }

    fun hasRequiredQrCodes(task: MontageTask): Boolean {
        val containsGatewayQrCode = task.lockerList
            .map { it.gatewaySerialnumber.isNotEmpty() }
            .contains(true)
        val hasEmptyQrCode = task.lockerList
            .map { it.qrCode.isEmpty() }
            .contains(true)

        return containsGatewayQrCode && !hasEmptyQrCode
    }

    fun setQrCodeForLocker(
        lifecycle: Lifecycle,
        lockerId: Int,
        qrCode: String,
        navigation: NavController? = null
    ) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            val result = databaseMontageTaskRepository.setLockerQrCode(qrCode, lockerId)
            if (result.hasData) {
                Log.v(TAG, "QR Code successfully added to database")
                changeState(State.Ready)
                navigation?.navigate(R.id.action_qr_code_fragment_to_landing_fragment)
            } else {
                changeState(State.Error)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    fun setGPSLocation(context: Context, lifecycle: Lifecycle) {
        changeState(State.Loading)

        val locationService = LocationServices.getFusedLocationProviderClient(context)
        locationService.lastLocation.apply {
            Log.d(TAG, "Trying LocationServices... isSuccessful - $isSuccessful")
            if (isSuccessful) {
                addOnSuccessListener {
                    Log.d(TAG, it.toString())
                    lifecycle.coroutineScope.launch {
                        _task.value?.location?.locationId?.let { id ->
                            databaseMontageTaskRepository.setGPSCoordinates(
                                it.longitude,
                                it.latitude,
                                id
                            )
                            changeState(State.Ready)
                        }
                    }
                    getTask(context, lifecycle)
                }
                addOnFailureListener {
                    Toast.makeText(context, "Failed to get GPS Location", Toast.LENGTH_LONG)
                        .show()
                    it.printStackTrace()
                    changeState(State.Ready)
                }
            } else {
                Toast.makeText(context, "Cant open GPS Service", Toast.LENGTH_SHORT).show()
                changeState(State.Ready)
            }
        }
    }

    fun resetLocationQrCode(
        lifecycle: Lifecycle,
        locationId: Int,
    ) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val result = databaseMontageTaskRepository.setLocationQrCode(locationId, "")
                if (result.hasData) {
                    val taskResult =
                        databaseMontageTaskRepository.getTaskById(_task.value?.montageTaskId!!)
                    if (taskResult.hasData) {
                        _task.value = taskResult.data
                    }
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

    fun registerLocker(
        lifecycle: Lifecycle,
        locationId: Int
    ) {
        changeState(State.Loading)
        Log.d(TAG, "getting locker QR Code")
        lifecycle.coroutineScope.launch {
            try {
                val result = networkMontageTaskRepository.getRegistrationQrCode(locationId)
                if (result.hasData) {
                    registrationQrCode = result.data!!
                    Log.d(TAG, "Registration code: $registrationQrCode")
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

    fun createQrCode(code: String): Bitmap {
        val squareDimen = 600
        val qrCode = MultiFormatWriter().encode(
            code,
            BarcodeFormat.QR_CODE,
            squareDimen,
            squareDimen
        )
        return Bitmap.createBitmap(
            IntStream.range(0, squareDimen).flatMap { h ->
                IntStream.range(0, squareDimen).map { w ->
                    if (qrCode[w, h]
                    ) Color.BLACK else Color.WHITE
                }
            }.toArray(),
            squareDimen, squareDimen, Bitmap.Config.ARGB_8888
        )
    }

    fun removeLockerQrCode(lifecycle: Lifecycle, context: Context, lockerId: Int) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val result = databaseMontageTaskRepository.setLockerQrCode("", lockerId)
                if (result.hasData) {
                    getTask(context, lifecycle)
                    changeState(State.Ready)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT)
                } else {
                    changeState(State.Ready)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Ready)
            }
        }
    }

    fun removeGatewayQrCode(lifecycle: Lifecycle, context: Context, lockerId: Int) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val result = databaseMontageTaskRepository.setGatewaySerialnumber("", lockerId)
                if (result.hasData) {
                    getTask(context, lifecycle)
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT)
                } else {
                    changeState(State.Ready)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Ready)
            }
        }
    }

    fun setGatewayForLocker(
        lifecycle: Lifecycle,
        lockerId: Int,
        serialnumber: String,
        navigation: NavController? = null
    ) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            val result =
                databaseMontageTaskRepository.setGatewaySerialnumber(serialnumber, lockerId)
            if (result.hasData) {
                gatewaySerialnumberList.add(serialnumber)
                qrCodeScannerState = QrCodeScannerState.Locker
                Log.d(TAG, "$gatewaySerialnumberList")
                Log.d(TAG, "$hasRegisteredGateway")
                changeState(State.Ready)
                navigation?.navigate(R.id.action_qr_code_fragment_to_landing_fragment)
            } else {
                changeState(State.Error)
            }
        }
    }

    fun qrCodeScreenNavigator(navigation: NavController, scannerState: QrCodeScannerState) {
        qrCodeScannerState = scannerState
        navigation.navigate(R.id.action_landing_fragment_to_qr_code_fragment)
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
        val mainActivityIntent = Intent(context, MainActivity::class.java)
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
                    context.startActivity(mainActivityIntent)
                    Toast.makeText(context, "No assigned Task available", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "No Task ID found", Toast.LENGTH_LONG).show()
                context.startActivity(mainActivityIntent)
            }
        }
    }

    fun revokeTask(context: Context, lifecycle: Lifecycle) {
        lifecycle.coroutineScope.launch {
            val response = networkMontageTaskRepository.setStatus(
                task.value?.montageTaskId!!,
                2
            )
            if (response.hasData) {
                val emptyLockers: List<Locker>? = task.value?.lockerList?.map { locker ->
                    Locker(
                        busSlot = 0,
                        columnNumber = locker.columnNumber,
                        gateway = locker.gateway,
                        gatewaySerialnumber = "",
                        locationId = locker.locationId,
                        lockerId = locker.lockerId,
                        lockerName = locker.lockerName ?: "",
                        lockerType = locker.lockerType,
                        montageTaskId = locker.montageTaskId,
                        qrCode = "",
                        typeName = locker.typeName ?: ""
                    )
                }
                if (!emptyLockers.isNullOrEmpty()) {
                    val res = networkMontageTaskRepository.registerLockers(emptyLockers)
                    if (res.hasData) Log.d(TAG, "registerLockers over revoke: ${res.data}")
                }

                task.value?.lockerList?.forEach {
                    databaseMontageTaskRepository.setGatewaySerialnumber("", it.lockerId)
                    databaseMontageTaskRepository.setBusSlot(it.lockerId, 0)
                    databaseMontageTaskRepository.setLockerQrCode("", it.lockerId)
                }

                context.dataStore.edit {
                    it[DataStoreConstants.ACTIVE_TASK_ID] = -1
                    it[DataStoreConstants.HAS_ACTIVE_TASK] = false
                }

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    fun submitLocationQrCode(
        lifecycle: Lifecycle,
        context: Context,
        navigation: NavController,
        qrCode: String,
        locationId: Int
    ) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val dbResponse = databaseMontageTaskRepository.setLocationQrCode(locationId, qrCode)
                if (dbResponse.hasData) {
                    val networkResponse =
                        networkMontageTaskRepository.registerLocation(locationId, qrCode)
                    if (networkResponse.hasData) {
                        val locationName: String =
                            networkResponse.data ?: throw Exception("Server sent no location name")
                        val dbNameResponse =
                            databaseMontageTaskRepository.setLocationName(locationId, locationName)
                        if (dbNameResponse.hasData) {
                            changeState(State.Ready)
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to save location name to Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        changeState(State.Ready)
                        Toast.makeText(
                            context,
                            "Couldn't send Qr Code to Server",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Couldn't save Location Qr Code", Toast.LENGTH_SHORT)
                        .show()
                    changeState(State.Ready)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            navigation.navigate(R.id.action_qr_code_fragment_to_landing_fragment)
        }
    }

    fun submitTaskData(lifecycle: Lifecycle, context: Context) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val safeTask = task.value
                    ?: throw Exception("registerLockers - Location ID not found")
                val lockers =
                    databaseMontageTaskRepository.getLockersByTaskId(safeTask.montageTaskId)

                if (lockers.hasData) {
                    val lockerNetworkResponse =
                        networkMontageTaskRepository.registerLockers(lockers.data!!)
                    if (lockerNetworkResponse.hasData) {
                        context.dataStore.edit {
                            it[DataStoreConstants.WORKFLOW_STATE] = "finished"
                        }
                        changeWorkflowState(WorkflowState.FINISHED)
                        changeState(State.Next)
                    } else {
                        Toast.makeText(
                            context,
                            lockerNetworkResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        changeState(State.Ready)
                    }
                } else {
                    Toast.makeText(context, "Locker List is empty", Toast.LENGTH_SHORT).show()
                    changeState(State.Error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Error)
            }
        }
    }

    fun checkGatewaySerial(serial: String): Boolean = serial.take(3) == "PCG"

    fun checkLocationQrCode(code: String): Boolean {
        return try {
            val id = cutUrlForLocationId(code)
            id.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    fun checkQrCodeForLocker(code: String): Boolean {
        return try {
            code.toLong()
            code.length == 15
        } catch (e: Exception) {
            false
        }
    }

    fun cutUrlForLocationId(code: String): String {
        //TODO: Errorhandling
        return try {
            val uri = Uri.parse(code)
            if (uri.authority != Constants.LOCATION_SCANNER_FLAG) "" else uri.getQueryParameter("qr")
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    fun closeTask(context: Context, lifecycle: Lifecycle) {
        changeState(State.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val safeTask = task.value ?: throw Exception("submitTask - Task is Null")
                val networkResponse =
                    networkMontageTaskRepository.setStatus(safeTask.montageTaskId, 4)
                if (networkResponse.hasData) {
                    val databaseResponse =
                        databaseMontageTaskRepository.setStatus(safeTask.montageTaskId, 4)
                    if (databaseResponse.hasData) {
                        context.dataStore.edit {
                            it[DataStoreConstants.ACTIVE_TASK_ID] = -1
                            it[DataStoreConstants.HAS_ACTIVE_TASK] = false
                            it[DataStoreConstants.WORKFLOW_STATE] = "scanning"
                        }
                        task.value = null
                        val intent = Intent(context, MainActivity::class.java)
                        changeState(State.Ready)
                        context.startActivity(intent)
                    } else {
                        changeState(State.Error)
                    }
                } else {
                    changeState(State.Error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(State.Error)
            }
        }
    }
}