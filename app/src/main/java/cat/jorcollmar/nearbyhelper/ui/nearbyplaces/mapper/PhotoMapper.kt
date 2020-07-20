package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.domain.model.PhotoDomain
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Photo
import javax.inject.Inject

class PhotoMapper @Inject constructor() : Mapper<PhotoDomain, Photo>() {
    override fun map(unmapped: PhotoDomain): Photo {
        return Photo(unmapped.photoReference, unmapped.height, unmapped.width)
    }
}