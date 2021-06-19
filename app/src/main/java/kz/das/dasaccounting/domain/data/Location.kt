package kz.das.dasaccounting.domain.data

import java.io.Serializable

data class Location(
    var long: Double,
    var lat: Double
): Serializable