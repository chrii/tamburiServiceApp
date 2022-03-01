package at.tamburi.tamburimontageservice.models

data class Locker(
    val lockerId: Int,
    val locationId: Int,
    val lockerName: String,
    val lockerType: Int,
    val columnNumber: Int,
    val montageTaskId: Int,
    val typeName: String,
    val gateway: Boolean,
    val gatewaySerialnumber: Int,
    val qrCode: String
)
//    companion object {
//        fun lockerIdToString(lockerList: List<Locker>): String = lockerList
//            .map { it.lockerId }
//            .toString()
//            .trim()
//            .replace("[", "")
//            .replace("]", "")
//
//        fun lockerIdList(ids: String): List<Int> = ids
//            .split(",")
//            .map { it.trim().toInt() }
//    }

