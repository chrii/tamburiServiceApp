package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_location")
data class RemoteLocationEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "location_id")
    val locationId: Int,
    @NonNull
    @ColumnInfo(name = "country_id")
    val countryId: Int,
    @NonNull
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @NonNull
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    @NonNull
    @ColumnInfo(name = "street")
    val street: String,
    @NonNull
    @ColumnInfo(name = "number")
    val number: String,
    @NonNull
    @ColumnInfo(name = "qr_code")
    val qrCode: String,
    @NonNull
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @NonNull
    @ColumnInfo(name = "country_name")
    val countryName: String
)