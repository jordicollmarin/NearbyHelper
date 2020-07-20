package cat.jorcollmar.data.mapper

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.PhotoData
import cat.jorcollmar.domain.model.PhotoDomain
import javax.inject.Inject

class PhotoDataMapper @Inject constructor() : Mapper<PhotoData, PhotoDomain>() {
    override fun map(unmapped: PhotoData): PhotoDomain {
        return PhotoDomain(unmapped.photoReference, unmapped.height, unmapped.width)
    }
}