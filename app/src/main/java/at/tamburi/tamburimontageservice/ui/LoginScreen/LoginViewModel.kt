package at.tamburi.tamburimontageservice.ui.LoginScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation

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
        return username == "Chris" && password == "1234"
    }
}