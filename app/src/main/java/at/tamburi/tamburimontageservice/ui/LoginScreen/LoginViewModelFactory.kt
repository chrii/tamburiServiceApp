package at.tamburi.tamburimontageservice.ui.LoginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.tamburi.tamburimontageservice.services.database.dao.UserDao
import java.lang.IllegalArgumentException

class LoginViewModelFactory(
    private val userDao: UserDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}