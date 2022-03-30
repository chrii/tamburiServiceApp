package at.tamburi.tamburimontageservice.services.network.dto

import at.tamburi.tamburimontageservice.models.MontageStatus
import com.google.gson.annotations.SerializedName

data class MontageTaskDto(
    @SerializedName("montageTaskId")
    var montageTaskId: Int?,
    @SerializedName("creationDate")
    var creationDate: String?,
    @SerializedName("location")
    var location: LocationDto?,
    @SerializedName("owner")
    var locationOwner: OwnerDto?,
    @SerializedName("locationDescription")
    var locationDescription: String?,
    @SerializedName("powerConnection")
    var powerConnection: String?,
    @SerializedName("montageGroundName")
    var montageGroundName: String?,
    @SerializedName("montageSketchUrl")
    var montageSketchUrl: String?,
    @SerializedName("servicemanList")
    var servicemanList: List<ServiceUserDto>?,
    @SerializedName("scheduledInstallationDate")
    var scheduledInstallationDate: String?,
    @SerializedName("montageHint")
    var montageHint: String?,
    @SerializedName("lockerList")
    var lockerList: List<LockerDto>?,
    @SerializedName("statusId")
    var statusId: Int?
)