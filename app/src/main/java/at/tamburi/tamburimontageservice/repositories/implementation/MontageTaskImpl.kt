package at.tamburi.tamburimontageservice.repositories.implementation

import at.tamburi.tamburimontageservice.mockdata.ownerMockList
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.IMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.dao.LocationOwnerDao
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import at.tamburi.tamburimontageservice.utils.DataState
import java.util.*

class MontageTaskImpl(
    private val montageTaskDao: MontageTaskDao,
    private val ownerDao: LocationOwnerDao
) : IMontageTaskRepository {
    override suspend fun getTaskById(id: Int): DataState<MontageTask> {
        return try {
            val result = montageTaskDao.getTaskByTaskId(id)
            if(result != null) {
                val task = MontageTask(
                    montageId = result.montageId,
                    createdAt = result.createdAt,
                    remoteLocationId = result.remoteLocationId,
                    remoteLocation = RemoteLocation(
                        locationId = 2,
                        countryId = 1,
                        cityId = 1,
                        zipCode = "1219",
                        streetName = "Floridusgasse",
                        streetNumber = "50",
                        qrCode = "a1b2",
                        minimumReservationTime = 5,
                        minimumPauseTime = 3
                    ),
                    magazine = result.magazine,
                    ownerId = result.ownerId,
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
                    lockerCount = result.lockerCount,
                    lockerTypeList = listOf(),
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

    override suspend fun getAllTasks(): DataState<List<MontageTask>> {
        return try {
            val result = montageTaskDao.getAllTasks()
            if (!result.isNullOrEmpty()) {
                val tasks = result.map {
                    val ownerEntity = ownerDao.getOwnerById(it.ownerId)

                    if (ownerEntity == null) {
                        //This is the owner request if the owner is not local
                        ownerMockList.find { i -> i.companyId == it.ownerId }
                    }
                    MontageTask(
                        montageId = it.montageId,
                        createdAt = it.createdAt,
                        remoteLocationId = it.remoteLocationId,
                        remoteLocation = RemoteLocation(
                            locationId = 2,
                            countryId = 1,
                            cityId = 1,
                            zipCode = "1219",
                            streetName = "Floridusgasse",
                            streetNumber = "50",
                            qrCode = "a1b2",
                            minimumReservationTime = 5,
                            minimumPauseTime = 3
                        ),
                        magazine = it.magazine,
                        ownerId = it.ownerId,
                        locationOwner = LocationOwner(
                            companyId = 1,
                            companyName = "GESIBA",
                            address = "Gesiba Straße",
                            streetNumber = "14",
                            zipCode = "1140"
                        ),
                        montageStatus = MontageStatus.values()[it.montageStatus],
                        locationDesc = it.locationDesc,
                        powerConnection = PowerConnection.values()[it.powerConnection],
                        montageGround = MontageGround.values()[it.montageGround.toInt()],
                        montageSketch = null,
                        lockerCount = it.lockerCount,
                        lockerTypeList = listOf(),
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
            DataState(
                hasData = false,
                data = null,
                message = e.localizedMessage
            )
        }
    }

    override suspend fun saveMockMontageTask(task: MontageTask) {
        try {
            val ownerById = ownerDao.getOwnerById(task.ownerId)
            if (ownerById == null) {
                ownerDao.saveOwner(
                    id = 1,
                    ownerId = 1,
                    companyName = "GESIBA",
                    address = "Gesiba Straße",
                    streetNumber = "14",
                    zipCode = "1140"
                )
            }

            val taskById = montageTaskDao.getTaskByTaskId(task.montageId)
            if (taskById == null) {
                montageTaskDao.saveTask(
                    id = task.montageId,
                    montageId = task.montageId,
                    createdAt = task.createdAt,
                    remoteLocationId = task.remoteLocationId,
                    magazine = "",
                    ownerId = task.ownerId,
                    montageStatus = task.montageStatus.ordinal,
                    locationDesc = task.locationDesc,
                    powerConnection = task.powerConnection.ordinal,
                    montageGround = task.montageGround.ordinal.toString(),
                    montageSketch = "",
                    lockerCount = 3,
                    lockerTypeList = task.lockerTypeList.map { it.typeID }
                        .toString()
                        .replace("[", "")
                        .replace("]", "")
                        .trim(),
                    assignedMonteurs = task.assignedMonteurs.toString(),
                    scheduledInstallation = task.scheduledInstallation
                )
            }
        } catch (e: Exception) {
            throw Exception(e.stackTraceToString())
        }
    }
}