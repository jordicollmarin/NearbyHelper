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

    override fun getAllNearbyPlaces(lat: String, lng: String): Observable<List<PlaceDomain>> =
        nearbyPlacesApiDataSource.getAllNearbyPlaces(lat, lng).map { placeDataMapper.map(it) }

    override fun getNearbyBars(): Observable<List<PlaceDomain>> {
        TODO("Not yet implemented")
    }

    override fun getNearbyCafes(): Observable<List<PlaceDomain>> {
        TODO("Not yet implemented")
    }

    override fun getNearbyRestaurants(): Observable<List<PlaceDomain>> {
        TODO("Not yet implemented")
    }
}