package at.tamburi.tamburimontageservice.services.network

import android.annotation.SuppressLint
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.services.network.dto.*
import at.tamburi.tamburimontageservice.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

fun ServiceUserDto.toServiceUser(date: Long, token: String? = null): ServiceUser = ServiceUser(
    servicemanId = servicemanId ?: throw Exception("No Service User Id received"),
    username = username ?: throw Exception("No Username received"),
    firstname = name ?: "No Name found",
    surname = surname ?: "No Surname found",
    phone = phone ?: "No Phone",
    email = email ?: "No Email Address",
    loginDate = date,
    token = token ?: ""
)

@SuppressLint("SimpleDateFormat")
fun MontageTaskDto.toMontageTask(): MontageTask {
    val crDate = SimpleDateFormat("yyyy-MM-dd").parse(creationDate)
        ?: throw Exception("Couldn't parse creation date")
    val schedDate = SimpleDateFormat("yyyy-MM-dd").parse(scheduledInstallationDate)
        ?: throw Exception("Couldn't parse scheduled date")
    return MontageTask(
        montageTaskId = montageTaskId ?: throw Exception("No Montage Task Id received"),
        creationDate = crDate,
        location = location?.toRemoteLocation() ?: throw Exception("No location data received"),
        locationOwner = locationOwner?.toLocationOwner(),
        statusId = statusId ?: throw Exception("No Montage Status received"),
        locationDescription = locationDescription ?: "No Description",
        powerConnection = powerConnection ?: throw Exception("No Power Connection Data received"),
        montageGroundName = (montageGroundName
            ?: Exception("No Ground Material received")) as String,
        montageSketchUrl = montageSketchUrl ?: "Not available",
        montageSketchImage = null,
        servicemanList = servicemanList?.map { it.toServiceUser(Date().time) }
            ?: throw Exception("No users for task received"),
        scheduledInstallationDate = schedDate,
        montageHint = montageHint ?: "No Montage Hint",
        lockerList = lockerList?.map { it.toLocker() } ?: throw Exception("No locker List received")
    )
}

fun LocationDto.toRemoteLocation(): MontageLocation = MontageLocation(
    locationId = locationId ?: -1,
    locationName = locationName ?: "No location name",
    countryId = countryId ?: -1,
    cityId = cityId ?: -1,
    zipCode = zipCode ?: "No Zip Code",
    street = street ?: "No street found",
    number = number ?: "No number found",
    qrCode = qrCode ?: "",
    cityName = cityName ?: "No city found",
    countryName = countryName ?: "No Country name",
    longitude = longitude ?: 0.0,
    latitude = latitude ?: 0.0,
    contactPerson = contactPerson ?: "No Contact Person",
    contactPhone = contactPhone ?: "No Phone"
)

fun OwnerDto.toLocationOwner(): LocationOwner = LocationOwner(
    buildingOwnerId = buildingOwnerId ?: -1,
    companyName = companyName ?: "No Company",
    name = name ?: "No Name",
    surname = surname ?: "No Surname",
    address = address ?: "No Address found",
    address2 = address2 ?: "",
    city = city ?: "No City",
    zipCode = zipCode ?: "No Zip Code",
    email = email ?: "No Email Address",
    phoneNumber = phoneNumber ?: "No Phone Number"
)

fun LockerDto.toLocker(): Locker = Locker(
    lockerId = lockerId ?: -1,
    locationId = locationId ?: -1,
    lockerName = lockerName ?: "No Locker Name",
    lockerType = lockerType ?: -1,
    columnNumber = columnNumber ?: -1,
    montageTaskId = montageTaskId ?: -1,
    typeName = typeName ?: "",
    gateway = gateway ?: false,
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

fun ClaimDto.toClaim(): Claim = Claim(
    claimId = claimId ?: throw Exception("Request no Claim Id found"),
    typeId = typeId ?: throw Exception("Request: No Type Id found"),
    typeName = typeName ?: throw Exception("Request: Typename not found"),
    tamburiCode = tamburiCode ?: throw Exception("Request: Tamburi Qr Code not found"),
    tamburiPin = tamburiPin ?: throw Exception("Request: Tamburi Pin not found"),
    compartmentWiring = compartmentWiring
        ?: throw Exception("Request: No compartment wiring found"),
    compartmentSize = compartmentSize ?: throw Exception("Size not found"),
    deliveryId = deliveryId ?: throw Exception("Request: No delivery ID found"),
    deliveryCode = deliveryCode
)

fun ClaimListObjectDto.toServiceAssignment(): ServiceAssignment = ServiceAssignment(
    assignmentId = assignmentId ?: throw Exception("Request: No assignment ID found"),
    location = location ?: throw Exception("Request: No Location information"),
    scheduledDate = if (scheduledDate != null) Utils.getDateFromString(scheduledDate) else throw Exception("Request: Has no scheduled Date"),
    servicemanId = servicemanId ?: throw Exception("Request: No Serviceman ID"),
)