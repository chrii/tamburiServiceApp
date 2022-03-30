package at.tamburi.tamburimontageservice.services.network.dto

import com.google.gson.annotations.SerializedName

data class OwnerDto(
    var buildingOwnerId: Int,
    var companyName: String,
    var name: String,
    var surname: String,
    var address: String,
    var address2: String,
    var city: String,
    @SerializedName("zipCodeId")
    var zipCode: String,
    var email: String?,
    var phoneNumber: String?
)