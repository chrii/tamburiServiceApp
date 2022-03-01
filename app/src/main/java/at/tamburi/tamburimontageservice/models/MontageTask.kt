package at.tamburi.tamburimontageservice.models

import android.graphics.Bitmap
import java.util.*

data class MontageTask(
    val montageTaskId: Int,
    val creationDate: Date,
    val location: RemoteLocation,
    val locationOwner: LocationOwner?,
    val montageStatus: String,
    val locationDescription: String,
    val powerConnection: String,
    val montageGroundName: String,
    var montageSketchUrl: String?,
    var montageSketchImage: Bitmap?,
    val servicemanList: List<ServiceUser>,
    val scheduledInstallationDate: Date,
    val montageHint: String,
    val lockerList: List<Locker>
)