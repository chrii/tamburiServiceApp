package at.tamburi.tamburimontageservice.utils

import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
val PermissionState.isPermanentlyRevoked: Boolean
    get() = !shouldShowRationale && !hasPermission