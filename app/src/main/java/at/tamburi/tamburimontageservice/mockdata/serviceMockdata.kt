package at.tamburi.tamburimontageservice.mockdata

import at.tamburi.tamburimontageservice.models.Claim
import at.tamburi.tamburimontageservice.models.ServiceAssignment
import at.tamburi.tamburimontageservice.models.Location
import java.util.*

class serviceMockdata {
    companion object {
        val serviceAssignments: List<ServiceAssignment> = listOf(
            ServiceAssignment(
                assignmentId = 1,
                locationId = 59,
                locationName = "Julia",
                locationAddress = "Rotenturmstraße",
                zipCode = "1010",
                city = "Wien",
                datum = Date()
            ),
            ServiceAssignment(
                assignmentId = 2,
                locationId = 2,
                locationName = "Sarah",
                locationAddress = "Linzer Straße",
                zipCode = "1140",
                city = "Wien",
                datum = Date()
            ),
            ServiceAssignment(
                assignmentId = 3,
                locationId = 61,
                locationName = "Gernot",
                locationAddress = "Khunngasse",
                zipCode = "1030",
                city = "Wien",
                datum = Date()
            ),
        )

        val locations: List<Location> = listOf(
            Location(
                active = true,
                atsamVersionId = 3,
                atsamVersionName = "1.01",
                batteryStatus = 0,
                cityId = 1,
                cityName = "Wien",
                colourId = 1,
                colourName = "blue",
                companyName = "GESIBA Gemeinnützige Bauaktiengesellschaft",
                contactPerson = "Hr. Müller",
                contactPhone = "06647854356",
                countryId = 1,
                countryName = "Österreich",
                esp32VersionId = 2,
                esp32VersionName = "0.72",
                hasOwner = true,
                latitude = 48.210533,
                locationId = 59,
                locationName = "Julia",
                longitude = 16.37479,
                minimumPauseTime = 0,
                minimumReservationTime = 0,
                newLocation = false,
                number = "12",
                ownerId = 30,
                qrCode = "http://tamburi.at/location?qr=CBBE9CF6-FF20-4A28-A844-FCE463E4E827",
                street = "Rotenturmstraße",
                zipCode = "1010",
                claimList = listOf(
                    Claim(
                        claimId = 1,
                        typeId = 1,
                        typeName = "Gestrandet",
                        tamburiCode = "ZXY-123-456",
                        tamburiPin = "12345678",
                    ),
                    Claim(
                        claimId = 2,
                        typeId = 2,
                        typeName = "Müll",
                        tamburiCode = "ABC-201-900",
                        tamburiPin = "12345679",
                    ),
                    Claim(
                        claimId = 3,
                        typeId = 2,
                        typeName = "Müll",
                        tamburiCode = "ABC-200-200",
                        tamburiPin = "12345671",
                    ),
                    Claim(
                        claimId = 4,
                        typeId = 1,
                        typeName = "Gestrandet",
                        tamburiCode = "ABC-200-200",
                        tamburiPin = "12345672",
                    ),

                )
            ),
            Location(
                active = true,
                atsamVersionId = 3,
                atsamVersionName = "1.01",
                batteryStatus = 0,
                cityId = 1,
                cityName = "Wien",
                colourId = 2,
                colourName = "white",
                companyName = "BUWOK GmbH",
                contactPerson = "Herr Pintarich",
                contactPhone = "066448337475",
                countryId = 1,
                countryName = "Österreich",
                esp32VersionId = 2,
                esp32VersionName = "0.72",
                hasOwner = true,
                latitude = 48.193096,
                locationId = 60,
                locationName = "Sarah",
                longitude = 16.314518,
                minimumPauseTime = 0,
                minimumReservationTime = 0,
                newLocation = false,
                number = "18",
                ownerId = 31,
                qrCode = "http://tamburi.at/location?qr=B48BB852-CE0B-4C7C-BA78-F0458CA7BA0A",
                street = "Linzer Straße",
                zipCode = "1140",
                claimList = listOf(
                    Claim(
                        claimId = 5,
                        typeId = 1,
                        typeName = "Gestrandet",
                        tamburiCode = "AAC-260-205",
                        tamburiPin = "12345681",
                    ),
                    Claim(
                        claimId = 6,
                        typeId = 3,
                        typeName = "Defekt",
                        tamburiCode = "ABY-220-240",
                        tamburiPin = "12345682",
                    ),
                    Claim(
                        claimId = 7,
                        typeId = 1,
                        typeName = "Gestrandet",
                        tamburiCode = "ABB-210-280",
                        tamburiPin = "12345683",
                    ),

                )
            ),
            Location(
                active = false,
                atsamVersionId = 3,
                atsamVersionName = "1.01",
                batteryStatus = 0,
                cityId = 1,
                cityName = "Wien",
                colourId = 3,
                colourName = "red",
                companyName = "GESIBA Gemeinnützige Bauaktiengesellschaft",
                contactPerson = "Jerabek Wolfgang",
                contactPhone = "06853485866",
                countryId = 1,
                countryName = "Österreich",
                esp32VersionId = 2,
                esp32VersionName = "0.72",
                hasOwner = true,
                latitude = 48.190235,
                locationId = 61,
                locationName = "Gernot",
                longitude = 16.389631,
                minimumPauseTime = 0,
                minimumReservationTime = 0,
                newLocation = false,
                number = "14-22",
                ownerId = 30,
                qrCode = "http://tamburi.at/location?qr=D67DCA57-2FA9-40A5-89EC-CA6D017DD9AB",
                street = "Khunngasse",
                zipCode = "1030",
                claimList = listOf(
                    Claim(
                        claimId = 8,
                        typeId = 3,
                        typeName = "Defekt",
                        tamburiCode = "ABA-206-210",
                        tamburiPin = "12345666",
                    ),
                    Claim(
                        claimId = 9,
                        typeId = 3,
                        typeName = "Defekt",
                        tamburiCode = "ACC-208-100",
                        tamburiPin = "12345668",
                    ),
                    Claim(
                        claimId = 10,
                        typeId = 3,
                        typeName = "Defekt",
                        tamburiCode = "CBC-100-100",
                        tamburiPin = "12345667",
                    ),
                    Claim(
                        claimId = 11,
                        typeId = 1,
                        typeName = "Gestrandet",
                        tamburiCode = "AVC-201-210",
                        tamburiPin = "12345669",
                    ),

                )
            )
        )
    }
}