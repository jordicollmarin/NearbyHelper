package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import cat.jorcollmar.domain.common.SchedulersFacade
import io.reactivex.Single
import javax.inject.Inject

class GetNearbyPlaceDetail @Inject constructor(
    schedulers: SchedulersFacade,
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract
) : BaseUseCase.RxSingleUseCase<PlaceDomain, GetNearbyPlaceDetail.Params>(
    schedulers
) {

    override fun build(params: Params): Single<PlaceDomain> {
        return nearbyPlacesRepository.getNearbyDetail(params.placeId)
    }

    class Params(val placeId: String)
}