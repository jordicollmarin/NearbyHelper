package cat.jorcollmar.data.model

data class PlaceData(
    val id: String?,
    val icon: String?,
    val name: String?,
    val rating: Double?,
    val userRatingsTotal: Long?,
    val openNow: Boolean?,
    val location: LocationData?,
    val photos: List<PhotoData>?
)