package cat.jorcollmar.data.source

import cat.jorcollmar.data.model.dto.GoogleApiResultDetailDto
import cat.jorcollmar.data.model.dto.GoogleApiResultListDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GooglePlacesWebservice {
    @GET(NEARBY_PLACE_SEARCH_ENDPOINT)
    fun getNearbyPlaces(@QueryMap params: Map<String, String>): Single<GoogleApiResultListDto>

    @GET(NEARBY_PLACE_DETAIL_ENDPOINT)
    fun getNearbyPlaceDetail(
        @Query("key") key: String,
        @Query("place_id") placeId: String,
        @Query("fields") fields: String
    ): Single<GoogleApiResultDetailDto>

    @GET(NEARBY_PLACE_SEARCH_ENDPOINT)
    fun getMoreResults(
        @Query("pageToken") pageToken: String,
        @Query("key") key: String
    ): Single<GoogleApiResultListDto>

    companion object {
        const val NEARBY_PLACE_SEARCH_ENDPOINT = "maps/api/place/nearbysearch/json"
        const val NEARBY_PLACE_DETAIL_ENDPOINT = "maps/api/place/details/json"
    }
}