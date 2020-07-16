package cat.jorcollmar.data.source

import cat.jorcollmar.data.model.dto.GoogleApiResultDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GooglePlacesWebservice {
    @GET(NEARBY_PLACE_SEARCH_ENDPOINT)
    fun getAllNearbyPlaces(@QueryMap params: Map<String, String>): Single<GoogleApiResultDto>

    @GET(NEARBY_PLACE_SEARCH_ENDPOINT)
    fun getMoreResults(
        @Query("pageToken") pageToken: String,
        @Query("key") key: String
    ): Single<GoogleApiResultDto>

    companion object {
        const val NEARBY_PLACE_SEARCH_ENDPOINT = "maps/api/place/nearbysearch/json"
    }

}