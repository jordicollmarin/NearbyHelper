package cat.jorcollmar.domain.repository

import cat.jorcollmar.domain.model.PlaceDomain
import io.reactivex.Observable
import io.reactivex.Single

interface NearbyPlacesRepositoryContract {
    fun getNearbyPlaces(lat: String, lng: String, placeType: String?): Observable<List<PlaceDomain>>
    fun getNearbyPlacesOrderedByDistance(lat: String, lng: String, placeType: String): Observable<List<PlaceDomain>>
    fun getNearbyDetail(placeId: String): Single<PlaceDomain>
}