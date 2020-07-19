package cat.jorcollmar.data.source.googleplaces

import android.util.Log
import cat.jorcollmar.data.BuildConfig
import cat.jorcollmar.data.mapper.dto.PlaceDtoMapper
import cat.jorcollmar.data.model.PlaceData
import cat.jorcollmar.data.source.GooglePlacesWebservice
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GooglePlacesApiDataSource @Inject constructor(
    private val googlePlacesWebservice: GooglePlacesWebservice,
    private val placeDtoMapper: PlaceDtoMapper
) {

    fun getNearbyPlaces(lat: String, lng: String, placeType: String?): Observable<List<PlaceData>> =
        googlePlacesWebservice.getNearbyPlaces(
            mutableMapOf(
                "location" to "$lat,$lng",
                "radius" to DEFAULT_RADIUS_VALUE,
                "key" to BuildConfig.GOOGLE_PLACES_API_KEY
            ).apply {
                placeType?.let { this["type"] = it }
            }
        ).flatMapObservable {
            Log.i(TAG, "${it.status} ${it.error_message}")
            it.results?.let { results ->
                Single.just(placeDtoMapper.map(results)).toObservable()
            }
        }

    fun getNearbyPlacesOrderedByDistance(
        lat: String,
        lng: String,
        placeType: String
    ): Observable<List<PlaceData>> =
        googlePlacesWebservice.getNearbyPlaces(
            mutableMapOf(
                "location" to "$lat,$lng",
                "key" to BuildConfig.GOOGLE_PLACES_API_KEY,
                "type" to placeType,
                "rankby" to RANK_BY_DISTANCE_VALUE
            )
        ).flatMapObservable {
            Log.i(TAG, "${it.status} ${it.error_message}")
            it.results?.let { results ->
                Single.just(placeDtoMapper.map(results)).toObservable()
            }
        }

    fun getNearbyPlaceDetail(placeId: String): Single<PlaceData> =
        googlePlacesWebservice.getNearbyPlaceDetail(
            BuildConfig.GOOGLE_PLACES_API_KEY,
            placeId,
            NEARBY_DETAIL_FIELDS
        ).map {
            Log.i(TAG, "${it.status} ${it.error_message}")
            it.result?.let { placeDto -> placeDtoMapper.map(placeDto) }
        }

    companion object {
        private const val TAG = "GooglePlacesApiDataSour"

        const val DEFAULT_RADIUS_VALUE = "1000"
        const val RANK_BY_DISTANCE_VALUE = "distance"
        const val NEARBY_DETAIL_FIELDS =
            "place_id,icon,name,price_level,international_phone_number,rating,user_ratings_total,opening_hours,geometry,photos"
    }
}