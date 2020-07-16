package cat.jorcollmar.data.model.dto

data class PlaceDto(
    val place_id: String,
    val icon: String?,
    val name: String?,
    val rating: Double?,
    val user_ratings_total: Long?,
    val opening_hours: OpeningHoursDto?,
    val geometry: GeometryDto?,
    val photos: List<PhotoDto>?
)