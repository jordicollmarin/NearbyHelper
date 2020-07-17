package cat.jorcollmar.domain.usecase.location

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.repository.LocationRepositoryContract
import com.mocadc.mocadc.domain.common.SchedulersFacade
import io.reactivex.Single
import javax.inject.Inject

class GetCurrentLocation @Inject constructor(
    schedulers: SchedulersFacade,
    private val locationRepository: LocationRepositoryContract
) : BaseUseCase.RxSingleUseCase<LocationDomain, GetCurrentLocation.Params>(
    schedulers
) {

    override fun build(params: Params): Single<LocationDomain> {
        return locationRepository.getCurrentLocation()
    }

    class Params()
}