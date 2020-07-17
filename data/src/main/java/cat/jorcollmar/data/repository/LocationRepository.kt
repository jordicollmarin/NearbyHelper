package cat.jorcollmar.data.repository

import cat.jorcollmar.data.mapper.LocationDataMapper
import cat.jorcollmar.data.source.googlefusedlocation.GoogleFusedLocationApiDataSource
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.repository.LocationRepositoryContract
import io.reactivex.Single
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val googleFusedLocationApiDataSource: GoogleFusedLocationApiDataSource,
    private val locationDataMapper: LocationDataMapper
) : LocationRepositoryContract {

    override fun getCurrentLocation(): Single<LocationDomain> =
        googleFusedLocationApiDataSource.locationObservable.map {
            locationDataMapper.map(it)
        }
}