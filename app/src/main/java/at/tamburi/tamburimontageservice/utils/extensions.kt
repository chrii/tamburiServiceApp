package at.tamburi.tamburimontageservice.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
val PermissionState.isPermanentlyRevoked: Boolean
    get() = !shouldShowRationale && !hasPermission