package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color as BitmapColor
import androidx.compose.ui.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseUserRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.ui.LoginActivity.LoginActivity
import at.tamburi.tamburimontageservice.ui.MontageWorkflowActivity.MontageWorkflowActivity
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.Utils
import at.tamburi.tamburimontageservice.utils.dataStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.stream.IntStream
import javax.inject.Inject


private const val TAG: String = "MainViewModel"

// TODO: REFACTOR STATE NAMES
enum class MainState {
    Loading,
    Error,
    Ready,
    NEXT
}

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val databaseUserRepo: IDatabaseUserRepository,
    private val taskRepoDatabase: IDatabaseMontageTaskRepository,
    private val taskNetworkRepo: INetworkMontageTaskRepository
) : ViewModel() {
    private val _mainState: MutableState<MainState> = mutableStateOf(MainState.Ready)
    private val _tasks: MutableState<List<MontageTask>> = mutableStateOf(listOf())
    private val _hasActiveTask: MutableState<Boolean> = mutableStateOf(false)
    private val _activeTask: MutableState<MontageTask?> = mutableStateOf(null)
    private val _filteredTasks: MutableState<List<MontageTask>> = mutableStateOf(_tasks.value)
    private val _serviceAssignmentList: MutableState<List<ServiceAssignment>> =
        mutableStateOf(listOf())
    private val _activeServiceLocation: MutableState<Location?> = mutableStateOf(null)

    val mainState: MutableState<MainState> = _mainState
    val filteredTasks: MutableState<List<MontageTask>> = _filteredTasks
    val activeTask: MutableState<MontageTask?> = _activeTask
    val activeServiceLocation: MutableState<Location?> = _activeServiceLocation
    val hasActiveTask: MutableState<Boolean> = _hasActiveTask
    val activeUser: MutableState<ServiceUser?> = mutableStateOf(null)
    val serviceAssignmentList: MutableState<List<ServiceAssignment>> = _serviceAssignmentList
    var loadingMessageString: String? = null
    var errorMessage: String = ""
    var taskDetailId: Int = 0

    fun changeState(state: MainState, loadingMessage: String? = null) {
        if (!loadingMessage.isNullOrEmpty()) loadingMessageString = loadingMessage
        _mainState.value = state
    }

    fun logout(
        lifecycle: Lifecycle,
        context: Context,
    ) {
        activeUser.value = null
        changeState(MainState.Loading)
        lifecycle.coroutineScope.launch {
            context.dataStore.edit {
                it[DataStoreConstants.ACTIVE_USER_ID] = 0
            }
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun getActiveUser(context: Context, lifecycle: Lifecycle) {
        changeState(MainState.Loading)

        lifecycle.coroutineScope.launch {
            try {
                val userId = getUserId(context)
                val user = databaseUserRepo.getUser(userId)
                if (user.hasData) {
                    activeUser.value = user.data
                    changeState(MainState.Ready)
                } else {
                    errorMessage = user.message ?: "Cannot find error message"
                    changeState(MainState.Error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(MainState.Error)
            }
        }
    }

    fun initializeMontageData(context: Context, lifecycle: Lifecycle) {
        changeState(MainState.Loading)
        lifecycle.coroutineScope.launch {
            val userId = getUserId(context)
            fetchAndSaveTasks(userId)
            if (_tasks.value.isNotEmpty()) {
                getActiveTask(context, userId)
            }
            changeState(MainState.Ready)
        }
    }

    fun initializeServiceData(context: Context, lifecycle: Lifecycle) {
        changeState(MainState.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val userId = activeUser.value?.servicemanId ?: throw Exception("User not found")
                val locationList = taskNetworkRepo.getClaimLocations(userId)

                if (locationList.hasData) {
                    serviceAssignmentList.value = locationList.data!!
                    changeState(MainState.Ready)
                } else {
                    errorMessage = locationList.message ?: "No Error message found"
                    changeState(MainState.Error)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(MainState.Error)
            }
        }
    }

    fun navigateToServiceLocation(
        locationId: Int,
        navigation: NavController
    ) {
        val assignment = serviceAssignmentList.value.find { it.location.locationId == locationId }
        activeServiceLocation.value = assignment?.location
        navigation.navigate(R.id.action_service_list_to_service_details)
    }

    fun getLocationClaims(lifecycle: Lifecycle) {
        loadingMessageString = "Get Claims"
        changeState(MainState.Loading)
        lifecycle.coroutineScope.launch {
            val locationId = activeServiceLocation.value?.locationId ?: -1
            if (locationId < 0) {
                errorMessage = "locationId not found"
                changeState(MainState.Error)
            } else {
                val response = taskNetworkRepo.getLocationClaims(locationId)

                if (response.hasData) {
                    activeServiceLocation.value?.claimList = response.data!!
                    changeState(MainState.Ready)
                } else {
                    errorMessage = response.message ?: "No message found"
                    changeState(MainState.Error)
                }
            }
        }
    }

    private suspend fun getUserId(context: Context): Int = context.dataStore.data.map {
        it[DataStoreConstants.ACTIVE_USER_ID]
    }.first() ?: throw Exception("No active user")


    private suspend fun fetchAndSaveTasks(userId: Int) {
        try {
            val t = taskNetworkRepo.getMontageTaskList(userId)
            if (t.hasData) {
                val dbTasks = taskRepoDatabase.saveTasks(t.data!!)
                if (dbTasks.hasData) {
                    _tasks.value = t.data!!
                    _filteredTasks.value = t.data!!
                } else {
                    throw Exception("Couldn't write Tasks to Database")
                }
            } else {
                throw Exception("Keine Auftragsdaten")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Cannot fetch or save tasks - ${e.message}")
        }
    }

    private suspend fun getActiveTask(context: Context, userId: Int) {
        try {
            val statsList = _tasks.value.filter { it.statusId == MontageStatus.ACTIVE }
            when {
                statsList.size > 1 -> {
                    resetOpenTasks()
                    hasActiveTask.value = false
                    fetchAndSaveTasks(userId)
                    Toast.makeText(
                        context,
                        "Zuviele aktive Aufträge - es werden alle zurückgesetzt",
                        Toast.LENGTH_LONG
                    ).show()
                }
                statsList.size == 1 -> {
                    val task = statsList.first()
                    val activeTaskId = context.dataStore.data.map {
                        it[DataStoreConstants.ACTIVE_TASK_ID]
                    }.first() ?: -1
                    if (activeTaskId == -1) {
                        context.dataStore.edit {
                            it[DataStoreConstants.ACTIVE_TASK_ID] = task.montageTaskId
                        }
                    }
                    _activeTask.value = task
                    _hasActiveTask.value = true
                }
                else -> {
                    _hasActiveTask.value = false
                }
            }
        } catch (e: Exception) {
            throw Exception("Error getting DataStore values")
        }
    }

    fun onSubmitTask(context: Context, lifecycle: Lifecycle) {
        if (hasActiveTask.value) {
            // TODO: Static Text
            Toast.makeText(context, "Aktiver Auftrag existiert bereits", Toast.LENGTH_SHORT).show()
        } else {
            setActiveTask(context, lifecycle)
            val intent = Intent(
                context,
                MontageWorkflowActivity::class.java
            )
            context.startActivity(intent)
        }
    }

    private suspend fun resetOpenTasks() {
        val tasks = _tasks.value.filter { it.statusId == MontageStatus.ACTIVE }
        Log.d(TAG, "resetOpenTasks - Count ${tasks.size}")
        if (tasks.isNotEmpty()) {
            tasks.map {
                val dbRes = taskRepoDatabase.setStatus(it.montageTaskId, MontageStatus.ASSIGNED)
                Log.d(
                    TAG,
                    "Updating DB Task Status Response for ID ${it.montageTaskId}: ${dbRes.hasData}"
                )
                val ntwRes = taskNetworkRepo.setStatus(it.montageTaskId, MontageStatus.ASSIGNED)
                Log.d(
                    TAG,
                    "Updating Network Task Status Response for ID ${it.montageTaskId}: ${ntwRes.hasData}"
                )
            }
        }
    }

    private fun setActiveTask(context: Context, lifecycle: Lifecycle) {
        lifecycle.coroutineScope.launch {
            try {
                val response = taskNetworkRepo.setStatus(
                    taskDetailId,
                    3
                )
                if (response.hasData) {
                    val dbResponse = taskRepoDatabase.setStatus(taskDetailId, 3)
                    if (dbResponse.hasData) {
                        Log.d(TAG, "Save active Task ID $taskDetailId to datastore")
                        context.dataStore.edit {
                            it[DataStoreConstants.ACTIVE_TASK_ID] = taskDetailId
                            it[DataStoreConstants.HAS_ACTIVE_TASK] = true
                        }
                        hasActiveTask.value = true
                    } else {
                        hasActiveTask.value = false
                    }
                } else {
                    hasActiveTask.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                changeState(MainState.Error)
            }
        }
    }

    fun hasGatewayAvailable(task: MontageTask): Boolean {
        return task.lockerList.map { it.gateway }.contains(true)
    }

    fun isInstallationDate(taskId: Int): Boolean {
        val task =
            _tasks.value.find { it.montageTaskId == taskId } ?: throw Exception("Task not found")
        val todaysDate = Utils.getDate()
        val installationDate = Utils.getDate(task.scheduledInstallationDate)
        return todaysDate == installationDate
    }

    fun checkBatteryStatus(battery: Int): Color = when {
        battery in 20..49 -> Color.Yellow
        battery < 20 -> Color.Red
        else -> Color.Green
    }

    fun openMaps(context: Context, lon: Double, lat: Double) {
        val uri = Uri.parse("google.navigation:q=$lat,$lon")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            setPackage("com.google.android.apps.maps")
        }
        context.startActivity(intent)
    }

    fun createQrCode(code: String): Bitmap {
        val qrCode = MultiFormatWriter().encode(
            code,
            BarcodeFormat.QR_CODE,
            300,
            300
        )
        return Bitmap.createBitmap(
            IntStream.range(0, 300).flatMap { h ->
                IntStream.range(0, 300).map { w ->
                    if (qrCode[w, h]
                    ) BitmapColor.BLACK else BitmapColor.WHITE
                }
            }.toArray(),
            300, 300, Bitmap.Config.ARGB_8888
        )
    }
}
