package at.tamburi.tamburimontageservice.utils

class DataState<T>(
    var hasData: Boolean = false,
    var message: String? = null,
    var data: T? = null
) {}