package cat.jorcollmar.data.repository

import cat.jorcollmar.data.mapper.PlaceDataMapper
import cat.jorcollmar.data.source.nearbyplaces.NearbyPlacesApiDataSource
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import io.reactivex.Observable
import javax.inject.Inject

class NearbyPlacesRepository @Inject constructor(
    private val nearbyPlacesApiDataSource: NearbyPlacesApiDataSource,
    private val placeDataMapper: PlaceDataMapper
) :
    NearbyPlacesRepositoryContract {

    override fun getNearbyPlaces(
        lat: String,
        lng: String,
        placeType: String?
    ): Observable<List<PlaceDomain>> =
        nearbyPlacesApiDataSource.getNearbyPlaces(lat, lng, placeType).map { placeDataMapper.map(it) }
}