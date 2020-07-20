package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model

import cat.jorcollmar.data.BuildConfig.GOOGLE_PLACES_API_KEY
import cat.jorcollmar.nearbyhelper.BuildConfig

data class Photo(
    val photoReference: String?,
    val height: Long?,
    val width: Long?
) {

    fun getPhotoUri(): String {
        return "${BuildConfig.GOOGLE_API_BASE_URL}$PHOTO_ENDPOINT?maxheight=$height&maxwidth=$width&photoreference=$photoReference&key=${GOOGLE_PLACES_API_KEY}"
    }

    companion object {
        const val PHOTO_ENDPOINT = "maps/api/place/photo"
    }
}