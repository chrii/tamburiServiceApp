package at.tamburi.tamburimontageservice.models

data class Locker(
    val lockerId: Int,
    val typeId: Int,
    val typeName: String,
    var qrCode: String,
    val gateway: Boolean
) {
    companion object {
        fun lockerIdToString(lockerList: List<Locker>): String = lockerList
            .map { it.lockerId }
            .toString()
            .trim()
            .replace("[", "")
            .replace("]", "")

        fun lockerIdList(ids: String): List<Int> = ids
            .split(",")
            .map { it.trim().toInt() }
    }
}
