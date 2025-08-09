package shachar.afeka.course.volunteers.models

import shachar.afeka.course.volunteers.utilities.Constants

data class Coordinates(
    val lat: Double = Constants.LocationDefault.LATITUDE,
    val lon: Double = Constants.LocationDefault.LONGITUDE,
) {
    class Builder(
        var lat: Double = Constants.LocationDefault.LATITUDE,
        var lon: Double = Constants.LocationDefault.LONGITUDE,
    ) {
        fun locationLat(lat: Double) = apply { this.lat = lat }
        fun locationLon(lon: Double) = apply { this.lon = lon }
        fun build() = Coordinates(lat, lon)
    }

    companion object {
        val Default = Coordinates()
    }
}