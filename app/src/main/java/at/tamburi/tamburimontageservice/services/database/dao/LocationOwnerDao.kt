package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.LocationOwnerEntity

@Dao
interface LocationOwnerDao {
    @Query("SELECT * FROM owner WHERE building_owner_id = :ownerId")
    suspend fun getOwnerById(ownerId: Int): LocationOwnerEntity?

    @Query(
        "INSERT INTO owner (building_owner_id, company_name, name, surname, address, address2, city, zip_code, email, phone_number)" +
                " VALUES (:buildingOwnerId,:companyName,:name,:surname,:address,:address2,:city,:zipCode,:email,:phoneNumber)"
    )
    suspend fun saveOwner(
        buildingOwnerId: Int,
        companyName: String,
        name: String,
        surname: String,
        address: String,
        address2: String,
        city: String,
        zipCode: String,
        email: String,
        phoneNumber: String
    ): Long
}
