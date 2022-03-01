package at.tamburi.tamburimontageservice.utils

class DataState<T>(
    var hasData: Boolean = false,
    var data: T? = null,
    var message: String? = null
) {}