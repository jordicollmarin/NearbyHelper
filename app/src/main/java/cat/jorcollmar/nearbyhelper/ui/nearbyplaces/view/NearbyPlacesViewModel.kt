package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.jorcollmar.domain.usecase.location.GetCurrentLocation
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaceDetail
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlacesOrderedByDistance
import cat.jorcollmar.nearbyhelper.commons.managers.PermissionManager
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.LocationMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Location
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesListFragment.Companion.FILTER_RESTAURANTS
import io.reactivex.functions.Consumer
import javax.inject.Inject

class NearbyPlacesViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocation,
    private val locationMapper: LocationMapper,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val getNearbyPlaceDetail: GetNearbyPlaceDetail,
    private val getNearbyPlacesOrderedByDistance: GetNearbyPlacesOrderedByDistance,
    private val placeMapper: PlaceMapper
) : ViewModel() {

    lateinit var currentLocation: Location
    lateinit var selectedPlaceId: String

    var selectedSortingOption: Int = SORT_RATING
    var selectedPlaceType: String? = null
        set(value) {
            field = value
            getNearbyPlacesList()
        }

    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace: LiveData<Place>
        get() = _selectedPlace

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>>
        get() = _places

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int>
        get() = _error

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            when (grantResults.first()) {
                PERMISSION_GRANTED -> onLocationPermissionGranted()
                PERMISSION_DENIED -> onLocationPermissionDenied()
            }
        }
    }

    fun onLocationPermissionGranted() {
        if (_places.value == null) {
            getCurrentLocation()
        }
    }

    private fun onLocationPermissionDenied() {
        _error.value = ERROR_PERMISSION_DENIED
    }

    fun getCurrentLocation() {
        _loading.value = true

        getCurrentLocation.execute(
            Consumer {
                _loading.value = false
                currentLocation = locationMapper.map(it)
                getNearbyPlacesList()
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get Location")
                _loading.value = false
                _error.value = ERROR_LOCATION
            },
            GetCurrentLocation.Params()
        )
    }

    fun getNearbyPlacesList() {
        if (selectedSortingOption == SORT_DISTANCE && selectedPlaceType != null) {
            getOrderedByDistanceList()
        } else {
            getUnOrderedList()
        }
    }

    private fun getUnOrderedList() {
        _loading.value = true

        getNearbyPlaces.execute(
            Consumer {
                _loading.value = false
                _places.value = applySorting(placeMapper.map(it))
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get nearby places")
                _loading.value = false
                _error.value = ERROR_NEARBY_PLACES
            },
            GetNearbyPlaces.Params(
                currentLocation.lat.toString(),
                currentLocation.lng.toString(),
                selectedPlaceType
            )
        )
    }

    private fun getOrderedByDistanceList() {
        _loading.value = true

        if (selectedPlaceType == null) {
            selectedPlaceType = FILTER_RESTAURANTS
        }

        getNearbyPlacesOrderedByDistance.execute(
            Consumer {
                _loading.value = false
                _places.value = placeMapper.map(it)
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get nearby places")
                _loading.value = false
                _error.value = ERROR_NEARBY_PLACES_DISTANCE
            },
            GetNearbyPlacesOrderedByDistance.Params(
                currentLocation.lat.toString(),
                currentLocation.lng.toString(),
                selectedPlaceType ?: FILTER_RESTAURANTS
            )
        )
    }

    fun getNearbyPlaceDetail() {
        _loading.value = true

        getNearbyPlaceDetail.execute(
            Consumer {
                _loading.value = false
                _selectedPlace.value = placeMapper.map(it)
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get nearby place detail")
                _loading.value = false
                _error.value = ERROR_NEARBY_PLACE_DETAIL
            },
            GetNearbyPlaceDetail.Params(selectedPlaceId)
        )
    }

    private fun applySorting(places: List<Place>?): List<Place> {
        places?.let { placesList ->
            return when (selectedSortingOption) {
                SORT_NAME -> placesList.sortedBy { it.name }
                SORT_OPEN_CLOSED -> placesList.sortedByDescending { it.openNow }
                SORT_DISTANCE -> placesList.sortedBy { it.location?.getDistance(currentLocation) }
                else -> placesList.sortedByDescending { it.rating }
            }
        } ?: run {
            return listOf()
        }
    }

    fun sortList() {
        if (selectedSortingOption == SORT_DISTANCE && selectedPlaceType != null) {
            getOrderedByDistanceList()
        } else {
            _places.value = applySorting(_places.value)
        }
    }

    override fun onCleared() {
        getNearbyPlaces.dispose()
        getNearbyPlacesOrderedByDistance.dispose()
        getCurrentLocation.dispose()
        getNearbyPlaceDetail.dispose()
        super.onCleared()
    }

    companion object {
        private const val TAG = "NearbyPlacesViewModel"

        const val ERROR_PERMISSION_DENIED = 1000
        const val ERROR_LOCATION = 1001
        const val ERROR_NEARBY_PLACES = 1002
        const val ERROR_NEARBY_PLACE_DETAIL = 1003
        const val ERROR_NEARBY_PLACES_DISTANCE = 1004

        const val SORT_RATING = 0
        const val SORT_NAME = 1
        const val SORT_DISTANCE = 2
        const val SORT_OPEN_CLOSED = 3
    }
}