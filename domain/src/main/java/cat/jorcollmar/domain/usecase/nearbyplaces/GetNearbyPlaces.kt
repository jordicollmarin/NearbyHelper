package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import com.mocadc.mocadc.domain.common.SchedulersFacade
import io.reactivex.Observable
import javax.inject.Inject

class GetNearbyPlaces @Inject constructor(
    schedulers: SchedulersFacade,
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract
) : BaseUseCase.RxObservableUseCase<List<PlaceDomain>, GetNearbyPlaces.Params>(
    schedulers
) {

    override fun build(params: Params): Observable<List<PlaceDomain>> {
        return nearbyPlacesRepository.getNearbyPlaces(params.lat, params.lng, params.placeType)
    }

    class Params(val lat: String, val lng: String, val placeType: String?)
}