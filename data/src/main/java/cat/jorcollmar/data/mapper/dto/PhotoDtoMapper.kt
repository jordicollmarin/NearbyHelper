package cat.jorcollmar.data.mapper.dto

import cat.jorcollmar.data.common.base.Mapper
import cat.jorcollmar.data.model.PhotoData
import cat.jorcollmar.data.model.dto.PhotoDto
import javax.inject.Inject

class PhotoDtoMapper @Inject constructor() : Mapper<PhotoDto, PhotoData>() {
    override fun map(unmapped: PhotoDto): PhotoData {
        return PhotoData(unmapped.photo_reference, unmapped.height, unmapped.width)
    }
}