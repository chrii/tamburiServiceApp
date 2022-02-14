package at.tamburi.tamburimontageservice.models

import android.graphics.Bitmap

data class MontageTask(
    val montageId: Int,
    val createdAt: Long,
    val remoteLocationId: Int,
    val remoteLocation: RemoteLocation,
    val magazine: String,
    val ownerId: Int,
    val locationOwner: LocationOwner,
    val montageStatus: MontageStatus,
    val locationDesc: String,
    val powerConnection: PowerConnection,
    val montageGround: MontageGround,
    val montageSketch: Bitmap?,
    val lockerCount: Int,
    val lockerTypeList: List<LockerType>,
    val assignedMonteurs: List<Int>,
    val scheduledInstallation: Long
)