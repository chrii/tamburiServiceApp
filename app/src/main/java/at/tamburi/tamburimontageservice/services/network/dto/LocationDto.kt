package at.tamburi.tamburimontageservice.services.network.dto

data class LocationDto(
    var locationId: Int,
    var countryId: Int,
    var cityId: Int,
    var zipCode: String,
    var street: String,
    var number: String,
    var qrCode: String,
    var cityName: String,
    var countryName: String
)