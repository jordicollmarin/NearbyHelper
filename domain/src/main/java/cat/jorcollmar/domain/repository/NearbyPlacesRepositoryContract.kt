package cat.jorcollmar.domain.repository

import cat.jorcollmar.domain.model.PlaceDomain
import io.reactivex.Observable

interface NearbyPlacesRepositoryContract {
    fun getNearbyPlaces(lat: String, lng: String, placeType: String?): Observable<List<PlaceDomain>>
}