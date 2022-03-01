package at.tamburi.tamburimontageservice.models

data class ServiceUser(
    val servicemanId: Int,
    val username: String,
    val firstname: String,
    val surname: String,
    val phone: String,
    val email: String,
    val loginDate: Long
) {
    companion object {
        fun toIdList(serviceUserList: List<ServiceUser>): String =
            serviceUserList.map { it.servicemanId }
                .toString()
                .trim()
                .replace("[", "")
                .replace("]", "")
    }
}