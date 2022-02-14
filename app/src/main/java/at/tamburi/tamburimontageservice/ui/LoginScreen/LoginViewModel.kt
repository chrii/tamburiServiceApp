package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.IMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.IUserRepository
import at.tamburi.tamburimontageservice.utils.ACTIVE_TASK_ID
import at.tamburi.tamburimontageservice.utils.HAS_ACTIVE_TASK
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

// TODO: REFACTOR STATENAME
enum class LoginState {
    Loading,
    Error,
    Ready,
    NEXT
}

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val userRepo: IUserRepository,
    private val taskRepo: IMontageTaskRepository
) : ViewModel() {
    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState.Ready)
    private val _tasks: MutableState<List<MontageTask>> = mutableStateOf(listOf())
    private val _hasActiveTask: MutableState<Boolean> = mutableStateOf(false)
    private var _activeTask: MontageTask? = null

    val loginState: MutableState<LoginState> = _loginState
    val tasks: MutableState<List<MontageTask>> = _tasks
    val activeTask: MontageTask? = _activeTask
    val hasActiveTask: MutableState<Boolean> = _hasActiveTask
    var loadingMessageString: String? = null
    var errorMessage: String = ""
    fun changeState(state: LoginState, loadingMessage: String? = null) {
        if (!loadingMessage.isNullOrEmpty()) loadingMessageString = loadingMessage
        _loginState.value = state
    }

    fun checkUserState(lifecycle: Lifecycle) {
        changeState(LoginState.Loading, "Get User Data")
        lifecycle.coroutineScope.launch {
            //TODO: Imitates loading delay - Delete if not necessary anymore
            delay(2000)

            val user = userRepo.getUser()
            if (!user.hasData) {
                changeState(LoginState.Ready)
            } else {
                val serviceUser = user.data
                val lastDate = getDate(Date(serviceUser!!.loginDate))
                val todayDate = getDate()

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

        //TODO: Hardcoded user
        if (lower == "chris" && password == "1234") {
            changeState(LoginState.Loading, "Login...")
            lifecycle.coroutineScope.launch {
                //TODO: Imitates loading delay - Delete of not necessary anymore
                delay(2000)
                try {
                    val date = Date().time
                    val serviceUser = ServiceUser(
                        userId = 1,
                        username = lower,
                        assignedMontageTaskId = null,
                        magazineId = null,
                        loginDate = date
                    )
                    val result = userRepo.saveUser(serviceUser)
                    Log.d(TAG, "${result.message}")
                    if (!result.hasData) changeState(LoginState.Error) else changeState(LoginState.NEXT)
                } catch (e: Exception) {
                    e.stackTrace
                    changeState(LoginState.Error)
                }
            }
        } else {
            errorMessage = "Password or username not correct"
            changeState(LoginState.Error)
        }
    }

    fun getTaskList(lifecycle: Lifecycle) {
        changeState(LoginState.Loading, "Hole Aufträge")
        lifecycle.coroutineScope.launch {
            try {
                //TODO: Put out the Mock Data
                taskRepo.saveMockMontageTask()
                val tasks = taskRepo.getAllTasks()
                if (tasks.hasData) {
                    Log.d(TAG, "${tasks.data}")
                    _tasks.value = tasks.data!!
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

    fun hasActiveTask(context: Context, lifecycle: Lifecycle) {
        Log.d(TAG, "Has active task?")
        lifecycle.coroutineScope.launch {
            try {
                val hasData: Boolean = context.dataStore.data.map {
                    it[HAS_ACTIVE_TASK] ?: false
                }.first()

                if (hasData) {
                    val activeTaskId: Int = context.dataStore.data.map {
                        it[ACTIVE_TASK_ID] ?: -1
                    }.first()

                    Log.d(TAG, "activeTaskId: $activeTaskId")
                    val activeTask: MontageTask? =
                        _tasks.value.find { it.montageId == activeTaskId }

                    if (activeTask != null) {
                        _activeTask = activeTask
                        _hasActiveTask.value = true
                    } else {
                        Toast.makeText(context, "No active Task found", Toast.LENGTH_SHORT).show()
                        _hasActiveTask.value = false
                    }
                } else {
                    Toast.makeText(context, "No active Task found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                throw Exception("Error getting DataStore values")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(mil: Date = Date()): Date {
        val simple = SimpleDateFormat("MM-dd")
        return simple.parse(simple.format(mil)) ?: throw Exception("Cannot parse date")
    }
}