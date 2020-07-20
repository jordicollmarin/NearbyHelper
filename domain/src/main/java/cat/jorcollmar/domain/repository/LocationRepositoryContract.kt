package cat.jorcollmar.domain.repository

import cat.jorcollmar.domain.model.LocationDomain
import io.reactivex.Single

interface LocationRepositoryContract {
    fun getCurrentLocation(): Single<LocationDomain>
}