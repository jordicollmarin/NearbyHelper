package cat.jorcollmar.data.source.nearbyplaces

import cat.jorcollmar.data.BuildConfig
import cat.jorcollmar.data.mapper.dto.PlaceDtoMapper
import cat.jorcollmar.data.model.PlaceData
import cat.jorcollmar.data.source.GooglePlacesWebservice
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class NearbyPlacesApiDataSource @Inject constructor(
    private val googlePlacesWebservice: GooglePlacesWebservice,
    private val placeDtoMapper: PlaceDtoMapper
) {

    private var currentPageToken: String = ""

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
            currentPageToken = it.next_page_token ?: ""
            it.results?.let { results ->
                Single.just(placeDtoMapper.map(results)).toObservable()
            }
        }

    fun getNextResults(): Observable<List<PlaceData>> =
        googlePlacesWebservice.getMoreResults(currentPageToken, BuildConfig.GOOGLE_PLACES_API_KEY)
            .flatMapObservable { googleApiResultsDto ->
                currentPageToken = googleApiResultsDto.next_page_token ?: ""
                googleApiResultsDto.results?.let { results ->
                    Single.just(placeDtoMapper.map(results)).toObservable()
                }
            }

    companion object {
        const val DEFAULT_RADIUS_VALUE = "5000"
    }
}