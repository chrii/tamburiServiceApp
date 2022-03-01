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
import at.tamburi.tamburimontageservice.MontageWorkflowActivity
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.database.IMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.database.IUserRepository
import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG: String = "LoginViewModel"

// TODO: REFACTOR STATE NAMES
enum class LoginState {
    Loading,
    Error,
    Ready,
    NEXT
}

enum class TaskListToggle {
    Doeb,
    Flo,
    All
}

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val userRepo: IUserRepository,
    private val taskRepo: IMontageTaskRepository,
    private val authRepo: IAuthenticationRepository,
    private val taskNetworkRepo: INetworkMontageTaskRepository
) : ViewModel() {
    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState.Ready)
    private val _tasks: MutableState<List<MontageTask>> = mutableStateOf(listOf())
    private val _hasActiveTask: MutableState<Boolean> = mutableStateOf(false)
    private val _activeTask: MutableState<MontageTask?> = mutableStateOf(null)
    private val _filteredTasks: MutableState<List<MontageTask>> = mutableStateOf(_tasks.value)

    val loginState: MutableState<LoginState> = _loginState
    val filteredTasks: MutableState<List<MontageTask>> = _filteredTasks
    val activeTask: MutableState<MontageTask?> = _activeTask
    val hasActiveTask: MutableState<Boolean> = _hasActiveTask
    var loadingMessageString: String? = null
    var errorMessage: String = ""
    var taskDetailId: Int = 0
    fun changeState(state: LoginState, loadingMessage: String? = null) {
        if (!loadingMessage.isNullOrEmpty()) loadingMessageString = loadingMessage
        _loginState.value = state
    }

    fun toggleTaskList(magazine: String) {
//        Log.d(TAG, "Magazine: ${_tasks.value.first().magazine}")
//        if (magazine != "all") {
//            val filtered = _tasks.value.filter { it.magazine == magazine }
//            _filteredTasks.value = filtered
//        } else {
//            _filteredTasks.value = _tasks.value
//        }
    }

    fun checkUserState(lifecycle: Lifecycle) {
        changeState(LoginState.Loading, "Get User Data")
        lifecycle.coroutineScope.launch {
            //TODO: Imitates loading delay - Delete if not necessary anymore
            val user = userRepo.getUser()
            if (!user.hasData) {
                changeState(LoginState.Ready)
            } else {
                Log.d(TAG, "userdata: ${user.hasData}")
                val serviceUser = user.data
                val lastDate = getDate(Date(serviceUser!!.loginDate))
                val todayDate = getDate()
                Log.d(TAG, "today: ${todayDate}")
                Log.d(TAG, "last: ${lastDate}")

                if (todayDate == lastDate) {
                    changeState(LoginState.NEXT)
                } else {
                    changeState(LoginState.Ready)
                }
            }
        }
    }

    fun onSubmit(username: String, password: String, lifecycle: Lifecycle) {
        val lower = username.lowercase(Locale.getDefault())
        changeState(LoginState.Loading, "Login...")
        lifecycle.coroutineScope.launch {
            //TODO: Imitates loading delay - Delete of not necessary anymore
            delay(2000)
            try {
                val networkUser = authRepo.getUser(lower, password)
                if (networkUser.hasData) {
                    val result = userRepo.saveUser(networkUser.data!!)
                    if (!result.hasData) {
                        changeState(LoginState.Error, result.message)
                    } else {
                        Log.d(TAG, "Got Service User: ${networkUser.data!!.username}")
                        changeState(LoginState.NEXT)
                    }
                } else {
                    changeState(LoginState.Error, networkUser.message)
                }
            } catch (e: Exception) {
                e.stackTrace
                changeState(LoginState.Error)
            }
        }
    }

    fun getTaskList(lifecycle: Lifecycle) {
        changeState(LoginState.Loading, "Hole Aufträge")
        lifecycle.coroutineScope.launch {
            try {
                //TODO: Put out the Mock Data
//                taskListMockData.map { taskRepo.saveMockMontageTask(it) }
                val t = taskNetworkRepo.getMontageTaskList(3)
                Log.d(TAG, "getTaskList: $t")
                val tasks = taskRepo.getAllTasks()
                if (tasks.hasData) {
                    Log.d(TAG, "${tasks.data}")
                    _tasks.value = tasks.data!!
                    _filteredTasks.value = tasks.data!!
                    changeState(LoginState.Ready)
                } else {
                    errorMessage = "Keine Auftragsdaten"
                    changeState(LoginState.Error)
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Empty error message on getTasksList()"
                changeState(LoginState.Error)
            }
        }
    }

    fun getActiveTask(context: Context, lifecycle: Lifecycle) {
        Log.d(TAG, "Has active task?")
        lifecycle.coroutineScope.launch {
            try {
                val hasData: Boolean = context.dataStore.data.map {
                    it[DataStoreConstants.HAS_ACTIVE_TASK] ?: false
                }.first()

                if (hasData) {
                    val activeTaskId: Int = context.dataStore.data.map {
                        it[DataStoreConstants.ACTIVE_TASK_ID] ?: -1
                    }.first()

                    Log.d(TAG, "activeTaskId: $activeTaskId")
                    val activeTask: MontageTask? =
                        _tasks.value.find { it.montageTaskId == activeTaskId }

                    if (activeTask != null) {
                        _activeTask.value = activeTask
                        _hasActiveTask.value = true
                    } else {
                        _hasActiveTask.value = false
                    }
                }
            } catch (e: Exception) {
                throw Exception("Error getting DataStore values")
            }
        }
    }

    fun onSubmitTask(context: Context, lifecycle: Lifecycle) {
        if (hasActiveTask.value) {
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

    fun setActiveTask(context: Context, lifecycle: Lifecycle) {
        lifecycle.coroutineScope.launch {
            context.dataStore.edit {
                it[DataStoreConstants.ACTIVE_TASK_ID] = taskDetailId
                it[DataStoreConstants.HAS_ACTIVE_TASK] = true
            }
            hasActiveTask.value = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(mil: Date = Date()): Date {
        val simple = SimpleDateFormat("MM-dd")
        return simple.parse(simple.format(mil)) ?: throw Exception("Cannot parse date")
    }
}