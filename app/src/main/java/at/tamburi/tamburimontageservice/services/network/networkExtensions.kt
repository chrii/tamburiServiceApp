package at.tamburi.tamburimontageservice.services.network

import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.services.network.dto.ServiceUserDto

fun ServiceUserDto.toServiceUser(date: Long): ServiceUser = ServiceUser(
    servicemanId = servicemanId,
    username = username,
    firstname = name,
    surname = surname,
    phone = phone,
    email = email,
    loginDate = date
)