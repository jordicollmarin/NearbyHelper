package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model

data class Place(
    val id: String,
    val icon: String?,
    val name: String?,
    val priceLevel: Int?,
    val internationalPhoneNumber: String?,
    val rating: Double?,
    val userRatingsTotal: Long?,
    val openNow: Boolean?,
    val location: Location?,
    val photos: List<Photo>?
) {

    fun getRatingFormatted(): String {
        return "$rating ($userRatingsTotal)"
    }
}