package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.jorcollmar.domain.usecase.location.GetCurrentLocation
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.nearbyhelper.commons.managers.PermissionManager
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.LocationMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Location
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import io.reactivex.functions.Consumer
import javax.inject.Inject

class NearbyPlacesViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocation,
    private val locationMapper: LocationMapper,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val placeMapper: PlaceMapper
) : ViewModel() {

    lateinit var currentLocation: Location
    lateinit var selectedPlace: Place
    var selectedSortingOption: Int = SORT_RATING
    var selectedPlaceType: String? = null
        set(value) {
            field = value
            getNearbyPlacesList()
        }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>>
        get() = _places

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            when (grantResults.first()) {
                PERMISSION_GRANTED -> onLocationPermissionGranted()
                PERMISSION_DENIED -> onLocationPermissionDenied()
            }
        }
    }

    fun onLocationPermissionGranted() {
        getCurrentLocation()
    }

    private fun onLocationPermissionDenied() {
        // TODO: Show error to user
    }

    private fun getCurrentLocation() {
        _loading.value = true

        getCurrentLocation.execute(
            Consumer {
                _loading.value = false
                currentLocation = locationMapper.map(it)
                getNearbyPlacesList()
            },
            Consumer {
                _loading.value = false
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get Location")
                // TODO: Show error to user
            },
            GetCurrentLocation.Params()
        )
    }

    private fun getNearbyPlacesList() {
        _loading.value = true

        getNearbyPlaces.execute(
            Consumer {
                _loading.value = false
                _places.value = applySorting(placeMapper.map(it))
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get nearby places")
                _loading.value = false
                _places.value = null
            },
            GetNearbyPlaces.Params(
                currentLocation.lat.toString(),
                currentLocation.lng.toString(),
                selectedPlaceType
            )
        )
    }

    private fun applySorting(places: List<Place>?): List<Place> {
        places?.let { placesList ->
            return when (selectedSortingOption) {
                SORT_NAME -> placesList.sortedBy { it.name }
                SORT_OPEN_CLOSED -> placesList.sortedByDescending { it.openNow }
                SORT_DISTANCE -> placesList.sortedBy {
                    it.location?.getDistance(
                        currentLocation
                    )
                }
                else -> placesList.sortedByDescending { it.rating }
            }
        } ?: run {
            return listOf()
        }
    }

    fun sortList() {
        _places.value = applySorting(_places.value)
    }

    override fun onCleared() {
        getNearbyPlaces.dispose()
        super.onCleared()
    }

    companion object {
        private const val TAG = "NearbyPlacesViewModel"

        const val SORT_RATING = 0
        const val SORT_NAME = 1
        const val SORT_DISTANCE = 2
        const val SORT_OPEN_CLOSED = 3
    }
}