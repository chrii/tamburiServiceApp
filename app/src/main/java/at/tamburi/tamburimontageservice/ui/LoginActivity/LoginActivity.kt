package at.tamburi.tamburimontageservice.ui.LoginActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import at.tamburi.tamburimontageservice.R
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "LoginActivity used")
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_login) as NavHostFragment
        navController = navHostFragment.navController
    }
}