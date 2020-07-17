package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model

import cat.jorcollmar.nearbyhelper.commons.utils.LocationUtils

data class Location(
    val lat: Double?,
    val lng: Double?
) {

    fun getDistance(locationToCompare: Location): Float? {
        return if (lat != null && lng != null) {
            LocationUtils.getDistanceDifference(android.location.Location("locationA").apply {
                latitude = lat
                longitude = lng
            }, android.location.Location("locationB").apply {
                latitude = locationToCompare.lat ?: 0.0
                longitude = locationToCompare.lng ?: 0.0
            })
        } else {
            null
        }
    }
}