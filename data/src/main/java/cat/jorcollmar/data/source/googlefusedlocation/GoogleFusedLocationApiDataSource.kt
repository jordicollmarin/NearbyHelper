package cat.jorcollmar.data.source.googlefusedlocation

import android.content.Context
import android.location.Location
import android.os.Looper
import cat.jorcollmar.data.mapper.dto.LocationDtoMapper
import cat.jorcollmar.data.model.LocationData
import cat.jorcollmar.data.model.dto.LocationDto
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleFusedLocationApiDataSource @Inject constructor(
    private val locationDtoMapper: LocationDtoMapper,
    context: Context
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationSubject = SingleSubject.create<LocationData>()
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        numUpdates = 1
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.locations?.get(0)?.let {
                setLocation(it)
            }
        }
    }

    val locationObservable: Single<LocationData> =
        locationSubject.doOnSubscribe { startLocationUpdates() }

    private fun startLocationUpdates() {
        fusedLocationClient.lastLocation.addOnSuccessListener(::setLocation)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun setLocation(location: Location) {
        locationSubject.onSuccess(
            locationDtoMapper.map(
                LocationDto(
                    location.latitude,
                    location.longitude
                )
            )
        )
    }
}