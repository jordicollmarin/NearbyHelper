package cat.jorcollmar.nearbyhelper.commons.utils

import android.location.Location

object LocationUtils {
    /**
     * Returns distance in KM from locationA to locationB
     *
     * @param locationA Location to compare locationB to
     * @param locationB Location to compare locationA to
     *
     * @return Distance in KM
     */
    fun getDistanceDifference(locationA: Location, locationB: Location): Float? {
        return metersToKm(locationA.distanceTo(locationB))
    }

    /**
     * Converts Meters to KM
     *
     * @param meters Meters to convert
     */
    private fun metersToKm(meters: Float): Float {
        return meters / 1000
    }
}