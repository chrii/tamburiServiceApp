package at.tamburi.tamburimontageservice.models

data class RemoteLocation(
    var locationId: Int,
    var countryId: Int,
    var cityId: Int,
    var zipCode: String,
    var street: String,
    var number: Int
    )