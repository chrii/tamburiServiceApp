package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.annotation.SuppressLint
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
import at.tamburi.tamburimontageservice.MontageWorkflowActivity
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageStatus
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseUserRepository
import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.Utils
import at.tamburi.tamburimontageservice.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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
    private val authRepo: IAuthenticationRepository,
    private val taskNetworkRepo: INetworkMontageTaskRepository
) : ViewModel() {
    private val _mainState: MutableState<MainState> = mutableStateOf(MainState.Ready)
    private val _tasks: MutableState<List<MontageTask>> = mutableStateOf(listOf())
    private val _hasActiveTask: MutableState<Boolean> = mutableStateOf(false)
    private val _activeTask: MutableState<MontageTask?> = mutableStateOf(null)
    private val _filteredTasks: MutableState<List<MontageTask>> = mutableStateOf(_tasks.value)

    val mainState: MutableState<MainState> = _mainState
    val filteredTasks: MutableState<List<MontageTask>> = _filteredTasks
    val activeTask: MutableState<MontageTask?> = _activeTask
    val hasActiveTask: MutableState<Boolean> = _hasActiveTask
    var activeUser: ServiceUser? = null
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
        navigation: NavController
    ) {
        activeUser = null
        changeState(MainState.Loading)
        lifecycle.coroutineScope.launch{
            context.dataStore.edit {
                it[DataStoreConstants.ACTIVE_USER_ID] = 0
            }
            navigation.navigate(R.id.action_task_list_to_login)
        }
    }

    fun onSubmit(username: String, password: String, lifecycle: Lifecycle, context: Context) {
        val lower = username.lowercase(Locale.getDefault())
        changeState(MainState.Loading, "Login...")
        lifecycle.coroutineScope.launch {
            try {
                val networkUser = authRepo.getUser(lower, password)
                if (networkUser.hasData) {
                    val result = databaseUserRepo.saveUser(networkUser.data!!)
                    if (!result.hasData) {
                        Log.d(TAG, "Login has no data")
                        changeState(MainState.Error, result.message)
                    } else {
                        context.dataStore.edit {
                            it[DataStoreConstants.ACTIVE_USER_ID] = networkUser.data!!.servicemanId
                        }
                        Log.d(TAG, "Got Service User: ${networkUser.data!!.username}")
                        changeState(MainState.NEXT)
                    }
                } else {
                    Log.d(TAG, "")
                    changeState(MainState.Error, networkUser.message)
                }
            } catch (e: Exception) {
                e.stackTrace
                changeState(MainState.Error)
            }
        }
    }

    fun getTaskList(lifecycle: Lifecycle, context: Context) {
        changeState(MainState.Loading, "Hole Aufträge")
        lifecycle.coroutineScope.launch {
            try {
                val userId = getUserId(context)
                fetchAndSaveTasks(userId)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = e.message ?: "Empty error message on getTasksList()"
                changeState(MainState.Error)
            }
        }
    }

    fun initializeData(context: Context, lifecycle: Lifecycle) {
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
                    if(activeTaskId == -1) {
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
        val task = _tasks.value.find { it.montageTaskId == taskId} ?: throw Exception("Task not found")
        val todaysDate = Utils.getDate()
        val installationDate = Utils.getDate(task.scheduledInstallationDate)
        return todaysDate == installationDate
    }
}
