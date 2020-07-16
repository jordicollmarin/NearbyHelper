package cat.jorcollmar.domain.repository

import cat.jorcollmar.domain.model.PlaceDomain
import io.reactivex.Observable

interface NearbyPlacesRepositoryContract {
    fun getAllNearbyPlaces(lat: String, lng: String): Observable<List<PlaceDomain>>
    fun getNearbyBars(): Observable<List<PlaceDomain>>
    fun getNearbyCafes(): Observable<List<PlaceDomain>>
    fun getNearbyRestaurants(): Observable<List<PlaceDomain>>
}