package cat.jorcollmar.domain.model

data class PlaceDomain(
    val id: String?,
    val icon: String?,
    val name: String?,
    val rating: Double?,
    val userRatingsTotal: Long?,
    val openNow: Boolean?,
    val location: LocationDomain?,
    val photos: List<PhotoDomain>?
)