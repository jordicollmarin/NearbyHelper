package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import javax.inject.Inject

class PlaceMapper @Inject constructor(
    private val locationMapper: LocationMapper,
    private val photoMapper: PhotoMapper
) : Mapper<PlaceDomain, Place>() {

    override fun map(unmapped: PlaceDomain): Place {
        return Place(
            unmapped.id,
            unmapped.icon,
            unmapped.name,
            unmapped.priceLevel,
            unmapped.internationalPhoneNumber,
            unmapped.rating,
            unmapped.userRatingsTotal,
            unmapped.openNow,
            unmapped.location?.let { locationMapper.map(it) },
            unmapped.photos?.let { photoMapper.map(it) }
        )
    }
}