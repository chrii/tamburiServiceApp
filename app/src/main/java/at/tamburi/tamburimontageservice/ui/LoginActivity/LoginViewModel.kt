package at.tamburi.tamburimontageservice.ui.ViewModels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseUserRepository
import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.ui.MainActivity.MainActivity
import at.tamburi.tamburimontageservice.utils.DataStoreConstants
import at.tamburi.tamburimontageservice.utils.Utils
import at.tamburi.tamburimontageservice.utils.dataStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.IntStream
import javax.inject.Inject

private const val TAG = "LoginViewModel"

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
    private val databaseUserRepo: IDatabaseUserRepository,
    private val authRepo: IAuthenticationRepository
) : ViewModel() {
    private val _state: MutableState<LoginState> = mutableStateOf(LoginState.Ready)

    val state: MutableState<LoginState> = _state

    var loadingMessageString: String? = null
    var errorMessage: String? = null

    fun changeState(state: LoginState, message: String? = null) {
        if (!message.isNullOrEmpty()) loadingMessageString = message
        _state.value = state
    }

    fun checkUserState(lifecycle: Lifecycle, context: Context) {
        changeState(LoginState.Loading)
        lifecycle.coroutineScope.launch {
            val userId = context.dataStore.data.map {
                it[DataStoreConstants.ACTIVE_USER_ID]
            }.first() ?: 0
            val user = databaseUserRepo.getUser(userId)
            if (!user.hasData) {
                changeState(LoginState.Ready)
            } else {
                val serviceUser = user.data
                val lastDate = Utils.getDate(Date(serviceUser!!.loginDate))
                val todayDate = Utils.getDate()

                if (todayDate == lastDate) {
                    changeState(LoginState.NEXT)
                } else {
                    changeState(LoginState.Ready)
                }
            }
        }
    }

    fun onSubmit(username: String, password: String, lifecycle: Lifecycle, context: Context) {
        changeState(LoginState.Loading)
        lifecycle.coroutineScope.launch {
            try {
                val networkUser = authRepo.getUser(username, password)
                if (networkUser.hasData) {
                    val result = databaseUserRepo.saveUser(networkUser.data!!)
                    if (!result.hasData) {
                        Log.d(TAG, "Login has no data")
                        errorMessage = result.message
                        changeState(LoginState.Error, result.message)
                    } else {
                        val token = networkUser.data?.token ?: throw Exception("Token not found")
                        Log.d(TAG, token)
                        context.dataStore.edit {
                            it[DataStoreConstants.ACTIVE_USER_ID] = networkUser.data!!.servicemanId
                            it[DataStoreConstants.SESSION_TOKEN] = token
                        }
                        Log.d(TAG, "Got Service User: ${networkUser.data!!.username}")
                        changeState(LoginState.NEXT)
                    }
                } else {
                    errorMessage = networkUser.message
                    changeState(LoginState.Error, networkUser.message)
                }
            } catch (e: Exception) {
                e.stackTrace
                changeState(LoginState.Error, "Error: Please contact Admin")
            }
        }
    }

    fun next(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
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
}