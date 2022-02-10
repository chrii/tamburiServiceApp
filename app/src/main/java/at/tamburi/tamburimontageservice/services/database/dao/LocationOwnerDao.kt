package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.LocationOwnerEntity

@Dao
interface LocationOwnerDao {
    @Query("SELECT * FROM owner WHERE id = :ownerId")
    suspend fun getOwnerById(ownerId: Int): LocationOwnerEntity?

    @Query(
        "INSERT INTO owner (id, owner_id, company_name, address, street_number, zip)" +
                " VALUES (:id, :ownerId, :companyName, :address, :streetNumber, :zipCode)"
    )
    suspend fun saveOwner(
        id: Int,
        ownerId: Int,
        companyName: String,
        address: String,
        streetNumber: String,
        zipCode: String
    ) : Long
}