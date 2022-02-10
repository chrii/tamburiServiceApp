package at.tamburi.tamburimontageservice

import android.app.Application
import at.tamburi.tamburimontageservice.services.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application()