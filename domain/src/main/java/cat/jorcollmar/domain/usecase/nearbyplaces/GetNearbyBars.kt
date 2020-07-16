package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import com.mocadc.mocadc.domain.common.SchedulersFacade
import io.reactivex.Observable

class GetNearbyBars(
    schedulers: SchedulersFacade,
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract
) : BaseUseCase.RxObservableUseCase<List<PlaceDomain>, GetNearbyBars.Params>(
    schedulers
) {

    override fun build(params: Params): Observable<List<PlaceDomain>> {
        return nearbyPlacesRepository.getNearbyBars()
    }

    class Params
}