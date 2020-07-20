package cat.jorcollmar.data.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.PlaceData
import cat.jorcollmar.domain.model.PlaceDomain
import javax.inject.Inject

class PlaceDataMapper @Inject constructor(
    private val locationDataMapper: LocationDataMapper,
    private val photoDataMapper: PhotoDataMapper
) : Mapper<PlaceData, PlaceDomain>() {

    override fun map(unmapped: PlaceData): PlaceDomain {
        return PlaceDomain(
            unmapped.id,
            unmapped.icon,
            unmapped.name,
            unmapped.priceLevel,
            unmapped.internationalPhoneNumber,
            unmapped.rating,
            unmapped.userRatingsTotal,
            unmapped.openNow,
            unmapped.location?.let { locationDataMapper.map(it) },
            unmapped.photos?.let { photoDataMapper.map(it) }
        )
    }
}