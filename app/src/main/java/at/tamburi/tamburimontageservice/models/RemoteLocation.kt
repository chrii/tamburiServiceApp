package at.tamburi.tamburimontageservice.models

import android.os.Parcelable

data class RemoteLocation(
    var locationName: String?,
    var qrCode: String?,
    val locationId: Int,
    val countryId: Int,
    val cityId: Int,
    val zipCode: String,
    val streetName: String,
    val streetNumber: String,
    val minimumReservationTime: Int,
    val minimumPauseTime: Int
)