package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.repositories.IMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG: String = "LoginViewModel"

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

    val loginState: MutableState<LoginState> = _loginState
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
            Log.d(TAG, "user: ${user.hasData}")
            if (!user.hasData) {
                changeState(LoginState.Ready)
            } else {
                val serviceUser = user.data
                val lastDate = getDate(Date(serviceUser!!.loginDate))
                val todayDate = getDate()

                if (todayDate == lastDate) {
                    Log.d(TAG, "Last Date: $lastDate")
                    Log.d(TAG, "Today Date: $todayDate")
                    taskRepo.saveMockMontageTask()
                    val tasks = taskRepo.getAllTasks()
                    Log.d(TAG, "Tasks: ${tasks.data}")
                    Log.d(TAG, "Tasks: ${tasks.message}")
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

    @SuppressLint("SimpleDateFormat")
    fun getDate(mil: Date = Date()): Date {
        val simple = SimpleDateFormat("MM-dd")
        return simple.parse(simple.format(mil)) ?: throw Exception("Cannot parse date")
    }
}