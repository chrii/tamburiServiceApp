package at.tamburi.tamburimontageservice.repositories.database.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.mockdata.ownerMockList
import at.tamburi.tamburimontageservice.mockdata.remoteLocationMockList
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.database.IMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.LockerDao
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import at.tamburi.tamburimontageservice.services.database.dao.RemoteLocationDao
import at.tamburi.tamburimontageservice.services.database.toLocationOwner
import at.tamburi.tamburimontageservice.services.database.toRemoteLocation
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "MontageTaskImpl"

class MontageTaskImpl(
    private val montageTaskDao: MontageTaskDao,
    private val ownerDao: LocationOwnerDao,
    private val lockerDao: LockerDao,
    private val locationDao: RemoteLocationDao
) : IMontageTaskRepository {
    override suspend fun getTaskById(id: Int): DataState<MontageTask> {
        return try {
            val result = montageTaskDao.getTaskByTaskId(id)
            if (result != null) {
                val ownerEntity = ownerDao.getOwnerById(result.ownerId)

                if (ownerEntity == null) {
                    //This is the owner request if the owner is not local
                    ownerMockList.find { i -> i.companyId == result.ownerId }
                }

                val lockerList = Locker.lockerIdList(result.lockerList).map { l ->
                    val r = lockerDao.getLockerById(l)
                    if (r != null) {
                        Locker(r.lockerId, r.typeId, r.typeName, r.qrCode, r.gateway)
                    } else throw Exception("One Locker not found")
                }
                val task = MontageTask(
                    montageId = result.montageId,
                    createdAt = result.createdAt,
                    remoteLocation = RemoteLocation(
                        locationId = 2,
                        countryId = 1,
                        cityId = 1,
                        zipCode = "1219",
                        streetName = "Floridusgasse",
                        streetNumber = "50",
                        qrCode = "",
                        locationName = "",
                        minimumReservationTime = 5,
                        minimumPauseTime = 3
                    ),
                    magazine = result.magazine,
                    locationOwner = LocationOwner(
                        companyId = 1,
                        companyName = "GESIBA",
                        address = "Gesiba Straße",
                        streetNumber = "14",
                        zipCode = "1140"
                    ),
                    montageStatus = MontageStatus.values()[result.montageStatus],
                    locationDesc = result.locationDesc,
                    powerConnection = PowerConnection.values()[result.powerConnection],
                    montageGround = MontageGround.values()[result.montageGround.toInt()],
                    montageSketch = null,
                    lockerList = lockerList,
                    assignedMonteurs = listOf(1),
                    scheduledInstallation = result.scheduledInstallation,
                )
                DataState(
                    hasData = true,
                    data = task,
                    message = "Got Task"
                )
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "No task with this ID assigned"
                )
            }
        } catch (e: Exception) {
            DataState(
                hasData = false,
                data = null,
                message = e.stackTraceToString()
            )
        }
    }

    override suspend fun setQrCode(qrCode: String, lockerId: Int): DataState<Boolean> {
        return try {
            lockerDao.setQrCode(qrCode, lockerId)
            DataState(
                hasData = true,
                data = true,
                message = "Success"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = false,
                message = "Error: Cant set QR Code for Locker"
            )
        }
    }

    override suspend fun getAllTasks(): DataState<List<MontageTask>> {
        return try {
            val result = montageTaskDao.getAllTasks()
            if (!result.isNullOrEmpty()) {
                val tasks = result.map {
                    val owner = ownerDao.getOwnerById(it.ownerId)?.toLocationOwner
                        ?: ownerMockList.find { i -> i.companyId == it.ownerId }
                        ?: throw Exception("Owner not found")

                    val location =
                        locationDao.getLocationById(it.remoteLocationId)?.toRemoteLocation
                            ?: remoteLocationMockList.find { i -> it.remoteLocationId == i.locationId }
                            ?: throw Exception("Remote Location not found")

                    val lockerList = Locker.lockerIdList(it.lockerList).map { l ->
                        val r = lockerDao.getLockerById(l)
                        if (r != null) {
                            Locker(r.lockerId, r.typeId, r.typeName, r.qrCode, r.gateway)
                        } else throw Exception("One Locker not found")
                    }
                    Log.d("TAG", "Owner: $owner")
                    Log.d("TAG", "Remote Location: $location")
                    Log.d("TAG", "LockerList: $lockerList")
                    MontageTask(
                        montageId = it.montageId,
                        createdAt = it.createdAt,
                        remoteLocation = location,
                        magazine = it.magazine,
                        locationOwner = owner,
                        montageStatus = MontageStatus.values()[it.montageStatus],
                        locationDesc = it.locationDesc,
                        powerConnection = PowerConnection.values()[it.powerConnection],
                        montageGround = MontageGround.values()[it.montageGround.toInt()],
                        montageSketch = null,
                        lockerList = lockerList,
                        assignedMonteurs = listOf(1),
                        scheduledInstallation = it.scheduledInstallation,
                    )
                }
                DataState(
                    hasData = true,
                    data = tasks,
                    message = "Got Tasks"
                )
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "Failed to fetch data"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            DataState(
                hasData = false,
                data = null,
                message = e.localizedMessage
            )
        }
    }

    override suspend fun saveMockMontageTask(task: MontageTask) {
        try {
            val ownerById = ownerDao.getOwnerById(task.locationOwner.companyId)
            val taskById = montageTaskDao.getTaskByTaskId(task.montageId)
            val locationById = locationDao.getLocationById(task.remoteLocation.locationId)
            task.lockerList.map {
                val r = lockerDao.getLockerById(it.lockerId)
                if (r == null) {
                    lockerDao.saveLocker(
                        it.lockerId,
                        it.typeId,
                        it.typeName,
                        it.qrCode,
                        it.gateway
                    )
                }
            }
            if (ownerById == null) {
                ownerDao.saveOwner(
                    id = task.locationOwner.companyId,
                    ownerId = task.locationOwner.companyId,
                    zipCode = task.locationOwner.zipCode,
                    streetNumber = task.locationOwner.streetNumber,
                    address = task.locationOwner.address,
                    companyName = task.locationOwner.companyName,
                )
            }

            if (locationById == null) {
                locationDao.saveLocation(
                    locationId = task.remoteLocation.locationId,
                    locationName = task.remoteLocation.locationName,
                    qrCode = task.remoteLocation.qrCode,
                    streetName = task.remoteLocation.streetName,
                    streetNumber = task.remoteLocation.streetNumber,
                    zipCode = task.remoteLocation.zipCode,
                    cityId = task.remoteLocation.cityId,
                    countryId = task.remoteLocation.countryId,
                    minimumReservationTime = task.remoteLocation.minimumReservationTime,
                    minimumPauseTime = task.remoteLocation.minimumPauseTime
                )
            }

            if (taskById == null) {
                montageTaskDao.saveTask(
                    id = task.montageId,
                    montageId = task.montageId,
                    ownerId = task.locationOwner.companyId,
                    remoteLocationId = task.remoteLocation.locationId,
                    createdAt = task.createdAt,
                    magazine = task.magazine,
                    montageStatus = task.montageStatus.ordinal,
                    locationDesc = task.locationDesc,
                    powerConnection = task.powerConnection.ordinal,
                    montageGround = task.montageGround.ordinal.toString(),
                    montageSketch = "",
                    lockerList = Locker.lockerIdToString(task.lockerList),
                    assignedMonteurs = task.assignedMonteurs.toString(),
                    scheduledInstallation = task.scheduledInstallation,
                )
            }
        } catch (e: Exception) {
            throw Exception(e.stackTraceToString())
        }
    }
}