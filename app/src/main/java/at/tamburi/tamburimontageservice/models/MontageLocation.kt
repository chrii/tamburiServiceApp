package at.tamburi.tamburimontageservice.models

data class MontageLocation(
    val locationId: Int,
    val locationName: String,
    val longitude: Double,
    val latitude: Double,
    val countryId: Int,
    val cityId: Int,
    val zipCode: String,
    val street: String,
    val number: String,
    val qrCode: String,
    val cityName: String,
    val countryName: String,
    val contactPerson: String,
    val contactPhone: String
)