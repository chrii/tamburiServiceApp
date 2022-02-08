package at.tamburi.tamburimontageservice

import android.app.Application
import at.tamburi.tamburimontageservice.services.database.AppDatabase

class BaseApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}