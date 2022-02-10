package at.tamburi.tamburimontageservice.services.database

import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.models.ServiceUser
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity


//TODO: Do not forget to add magazine id if its here
val UserEntity.toServiceUser: ServiceUser
    get() = ServiceUser(
        userId = userId,
        username = username,
        loginDate = loginDate,
        magazineId = null,
        assignedMontageTaskId = assignedTask
    )

//val MontageTaskEntity.toMontageTask: MontageTask
//    get() = MontageTask(
//
//    )