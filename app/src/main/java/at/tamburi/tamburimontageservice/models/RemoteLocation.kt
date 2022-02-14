package at.tamburi.tamburimontageservice.models

import android.os.Parcelable

data class RemoteLocation(
    val locationId: Int,
    val countryId: Int,
    val cityId: Int,
    val zipCode: String,
    val streetName: String,
    val streetNumber: String,
    val qrCode: String?,
    val minimumReservationTime: Int,
    val minimumPauseTime: Int
)