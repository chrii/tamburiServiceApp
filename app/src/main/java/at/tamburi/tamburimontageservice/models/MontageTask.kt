package at.tamburi.tamburimontageservice.models

import android.graphics.Bitmap

data class MontageTask(
    val montageId: Int,
    val createdAt: Long,
    val remoteLocation: RemoteLocation,
    val magazine: String,
    val locationOwner: LocationOwner,
    val montageStatus: MontageStatus,
    val locationDesc: String,
    val powerConnection: PowerConnection,
    val montageGround: MontageGround,
    val montageSketch: Bitmap?,
    val lockerList: List<Locker>,
    val assignedMonteurs: List<Int>,
    val scheduledInstallation: Long
)