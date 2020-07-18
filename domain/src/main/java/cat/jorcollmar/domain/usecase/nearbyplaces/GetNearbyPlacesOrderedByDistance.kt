package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import cat.jorcollmar.domain.common.SchedulersFacade
import io.reactivex.Observable
import javax.inject.Inject

class GetNearbyPlacesOrderedByDistance @Inject constructor(
    schedulers: SchedulersFacade,
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract
) : BaseUseCase.RxObservableUseCase<List<PlaceDomain>, GetNearbyPlacesOrderedByDistance.Params>(
    schedulers
) {

    override fun build(params: Params): Observable<List<PlaceDomain>> {
        return nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(params.lat, params.lng, params.placeType)
    }

    class Params(val lat: String, val lng: String, val placeType: String)
}