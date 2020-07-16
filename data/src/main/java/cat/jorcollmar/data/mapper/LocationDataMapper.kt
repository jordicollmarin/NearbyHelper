package cat.jorcollmar.data.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.LocationData
import cat.jorcollmar.domain.model.LocationDomain
import javax.inject.Inject

class LocationDataMapper @Inject constructor() : Mapper<LocationData, LocationDomain>() {
    override fun map(unmapped: LocationData): LocationDomain {
        return LocationDomain(unmapped.lat, unmapped.lng)
    }
}