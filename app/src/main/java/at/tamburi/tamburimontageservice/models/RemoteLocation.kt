package at.tamburi.tamburimontageservice.models

import android.os.Parcelable

data class RemoteLocation(
    val locationId: Int,
    val countryId: Int,
    val cityId: Int,
    val zipCode: String,
    val street: String,
    val number: String,
    val qrCode: String,
    val cityName: String,
    val countryName: String
)