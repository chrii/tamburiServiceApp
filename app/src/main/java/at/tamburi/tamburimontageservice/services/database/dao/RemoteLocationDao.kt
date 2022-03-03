package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.RemoteLocationEntity

@Dao
interface RemoteLocationDao {
    @Query("SELECT * FROM remote_location WHERE location_id = :locationId")
    suspend fun getLocationById(locationId: Int): RemoteLocationEntity?

    @Query(
        "INSERT INTO remote_location (location_id, country_id, city_id, zip_code, street, number, qr_code, country_name) " +
                "VALUES (:locationId,:countryId,:cityId,:zipCode,:street,:number,:qrCode,:cityName,:countryName)"
    )
    suspend fun saveLocation(
        locationId: Int,
        countryId: Int,
        cityId: Int,
        zipCode: String,
        street: String,
        number: String,
        qrCode: String,
        cityName: String,
        countryName: String
    ): Int
}
