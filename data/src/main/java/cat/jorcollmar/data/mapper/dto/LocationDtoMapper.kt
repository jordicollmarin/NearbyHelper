package cat.jorcollmar.data.mapper.dto

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.LocationData
import cat.jorcollmar.data.model.dto.LocationDto
import javax.inject.Inject

class LocationDtoMapper @Inject constructor() : Mapper<LocationDto, LocationData>() {
    override fun map(unmapped: LocationDto): LocationData {
        return LocationData(unmapped.lat, unmapped.lng)
    }
}