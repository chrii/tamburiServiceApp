package at.tamburi.tamburimontageservice.services.network

import android.annotation.SuppressLint
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.services.network.dto.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun ServiceUserDto.toServiceUser(date: Long): ServiceUser = ServiceUser(
    servicemanId = servicemanId,
    username = username,
    firstname = name,
    surname = surname,
    phone = phone ?: "No Phone",
    email = email ?: "No Email Address",
    loginDate = date
)

@SuppressLint("SimpleDateFormat")
fun MontageTaskDto.toMontageTask(): MontageTask {
    val crDate = SimpleDateFormat("yyyy-MM-dd").parse(creationDate)
    val schedDate = SimpleDateFormat("yyyy-MM-dd").parse(scheduledInstallationDate)
    return MontageTask(
        montageTaskId = montageTaskId,
        creationDate = crDate,
        location = location.toRemoteLocation(),
        locationOwner = locationOwner?.toLocationOwner(),
        montageStatus = "MontageStatus.CREATED",
        locationDescription = locationDescription,
        powerConnection = powerConnection,
        montageGroundName = montageGroundName,
        montageSketchUrl = montageSketchUrl ?: "Not available",
        montageSketchImage = null,
        servicemanList = servicemanList.map { it.toServiceUser(Date().time) },
        scheduledInstallationDate = schedDate,
        montageHint = montageHint,
        lockerList = lockerList.map { it.toLocker() }
    )
}

fun LocationDto.toRemoteLocation(): RemoteLocation = RemoteLocation(
    locationId = locationId,
    locationName = locationName,
    countryId = countryId,
    cityId = cityId,
    zipCode = zipCode,
    street = street,
    number = number,
    qrCode = qrCode ?: "",
    cityName = cityName,
    countryName = countryName
)

fun OwnerDto.toLocationOwner(): LocationOwner = LocationOwner(
    buildingOwnerId = buildingOwnerId,
    companyName = companyName,
    name = name,
    surname = surname,
    address = address,
    address2 = address2,
    city = city,
    zipCode = zipCode,
    email = email ?: "No Email Address",
    phoneNumber = phoneNumber ?: "No Phone Number"
)

fun LockerDto.toLocker(): Locker = Locker(
    lockerId = lockerId,
    locationId = locationId,
    lockerName = lockerName,
    lockerType = lockerType,
    columnNumber = columnNumber,
    montageTaskId = montageTaskId,
    typeName = typeName ?: "",
    gateway = gateway,
    gatewaySerialnumber = gatewaySerialnumber ?: "",
    qrCode = qrCode ?: "",
    busSlot = 0
)

fun Locker.toLockerRegistrationDto(): LockerRegistrationDto = LockerRegistrationDto(
    lockerId = lockerId,
    qrCode = qrCode,
    gatewaySerialNumber = gatewaySerialnumber,
    busSlot = busSlot
        ?: throw Exception("Cannot transform Locker to Locker Registration - Bus Slot is null")
)
