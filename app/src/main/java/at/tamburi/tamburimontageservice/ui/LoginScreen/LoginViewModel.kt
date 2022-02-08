package at.tamburi.tamburimontageservice.ui.LoginScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import java.util.*

enum class LoginState {
    Loading,
    Error,
    Ready
}

class LoginViewModel: ViewModel() {
    private val _loginState: MutableState<LoginState> = mutableStateOf(LoginState.Ready)

    val loginState: MutableState<LoginState> = _loginState

    fun changeState(state: LoginState) {
        _loginState.value = state
    }

    fun onSubmit(username: String, password: String): Boolean {
        val lower = username.lowercase(Locale.getDefault())
        return lower == "chris" && password == "1234"
    }
}