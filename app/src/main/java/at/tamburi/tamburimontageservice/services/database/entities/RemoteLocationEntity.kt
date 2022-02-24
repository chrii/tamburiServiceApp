package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_location")
data class RemoteLocationEntity(
    @PrimaryKey
    @ColumnInfo(name = "location_id")
    val locationId: Int,

    @NonNull
    @ColumnInfo(name = "qr_code")
    val qrCode: String,

    @NonNull
    @ColumnInfo(name = "location_name")
    val locationName: String,

    @NonNull
    @ColumnInfo(name = "country_id")
    val countryId: Int,

    @NonNull
    @ColumnInfo(name = "city_id")
    val cityId: Int,

    @NonNull
    @ColumnInfo(name = "zip")
    val zipCode: String,

    @NonNull
    @ColumnInfo(name = "street_name")
    val streetName: String,

    @NonNull
    @ColumnInfo(name = "street_number")
    val streetNumber: String,

    @NonNull
    @ColumnInfo(name = "minimum_reservation_time")
    val minimumReservationTime: Int,

    @NonNull
    @ColumnInfo(name = "minimum_pause_time")
    val minimumPauseTime: Int
)