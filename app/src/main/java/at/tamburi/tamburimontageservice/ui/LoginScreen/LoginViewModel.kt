package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity
import at.tamburi.tamburimontageservice.services.database.entities.toServiceUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val TAG: String = "LoginViewModel"

enum class LoginState {
    Loading,
    Error,
    Ready,
    NEXT
}

class LoginViewModel(private val userDao: UserDao) : ViewModel() {
    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState.Ready)

    val loginState: MutableState<LoginState> = _loginState
    var loadingMessageString: String? = null
    fun changeState(state: LoginState, loadingMessage: String? = null) {
        if(!loadingMessage.isNullOrEmpty()) loadingMessageString = loadingMessage
        _loginState.value = state
    }


    fun checkUserState(lifecycle: Lifecycle) {
        changeState(LoginState.Loading, "Get User Data")
        lifecycle.coroutineScope.launch {
            //TODO: Imitates loading delay - Delete if not necessary anymore
            delay(2000)

            val entity = userDao.getUserData()
            if (entity.isNullOrEmpty()) {
                changeState(LoginState.Ready)
            } else {
                val serviceUser = entity.first().toServiceUser
                val lastDate = getDate(Date(serviceUser.loginDate))
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
                val cal = Calendar.getInstance().timeInMillis
                val userState = userDao.saveUserEntry(lower, 1, cal)
                Log.d(TAG, "$userState")
                changeState(LoginState.NEXT)
            }
        } else {
            changeState(LoginState.Error)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(mil: Date = Date()): Date {
        val simple = SimpleDateFormat("MM-dd")
        return simple.parse(simple.format(mil)) ?: throw Exception("Cannot parse today date")
    }
}