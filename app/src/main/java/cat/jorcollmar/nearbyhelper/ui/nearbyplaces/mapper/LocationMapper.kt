package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Location
import javax.inject.Inject

class LocationMapper @Inject constructor() : Mapper<LocationDomain, Location>() {
    override fun map(unmapped: LocationDomain): Location {
        return Location(unmapped.lat, unmapped.lng)
    }
}