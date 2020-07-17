package cat.jorcollmar.data.repository

import cat.jorcollmar.data.mapper.PlaceDataMapper
import cat.jorcollmar.data.source.googleplaces.GooglePlacesApiDataSource
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class NearbyPlacesRepository @Inject constructor(
    private val googlePlacesApiDataSource: GooglePlacesApiDataSource,
    private val placeDataMapper: PlaceDataMapper
) :
    NearbyPlacesRepositoryContract {

    override fun getNearbyPlaces(
        lat: String,
        lng: String,
        placeType: String?
    ): Observable<List<PlaceDomain>> =
        googlePlacesApiDataSource.getNearbyPlaces(lat, lng, placeType)
            .map { placeDataMapper.map(it) }

    override fun getNearbyDetail(placeId: String): Single<PlaceDomain> =
        googlePlacesApiDataSource.getNearbyPlaceDetail(placeId).map { placeDataMapper.map(it) }

}