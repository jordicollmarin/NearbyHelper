package cat.jorcollmar.data.mapper.dto

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.PlaceData
import cat.jorcollmar.data.model.dto.PlaceDto
import javax.inject.Inject

class PlaceDtoMapper @Inject constructor(
    private val locationDtoMapper: LocationDtoMapper,
    private val photoDtoMapper: PhotoDtoMapper
) : Mapper<PlaceDto, PlaceData>() {

    override fun map(unmapped: PlaceDto): PlaceData {
        return PlaceData(
            unmapped.place_id,
            unmapped.icon,
            unmapped.name,
            unmapped.price_level,
            unmapped.international_phone_number,
            unmapped.rating,
            unmapped.user_ratings_total,
            unmapped.opening_hours?.open_now,
            unmapped.geometry?.location?.let { locationDtoMapper.map(it) },
            unmapped.photos?.let { photoDtoMapper.map(it) }
        )
    }
}