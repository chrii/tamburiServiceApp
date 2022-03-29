package at.tamburi.tamburimontageservice.services.network.dto

import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("montageTaskId")
    val montageTaskId: Int,
    @SerializedName("statusId")
    val statusId: Int
)