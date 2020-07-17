package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import io.reactivex.functions.Consumer
import javax.inject.Inject

class NearbyPlacesViewModel @Inject constructor(
    private val getNearbyPlaces: GetNearbyPlaces,
    private val placeMapper: PlaceMapper
) : ViewModel() {

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

    init {
        getNearbyPlacesList()
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
            // TODO: Get latitude and longitude from viewModelVariable currentPosition
            GetNearbyPlaces.Params("-33.8670522", "151.1957362", selectedPlaceType)
        )
    }

    private fun applySorting(places: List<Place>): List<Place> {
        return when (selectedSortingOption) {
            SORT_NAME -> places.sortedBy { it.name }
            SORT_OPEN_CLOSED -> places.sortedBy { it.openNow }.reversed()
            else -> places.sortedBy { it.rating }.reversed()
        }
    }

    fun sortList() {
        _places.value = when (selectedSortingOption) {
            SORT_NAME -> _places.value?.sortedBy { it.name }
            SORT_OPEN_CLOSED -> _places.value?.sortedBy { it.openNow }?.reversed()
            else -> _places.value?.sortedBy { it.rating }?.reversed()
        }
    }

    override fun onCleared() {
        getNearbyPlaces.dispose()
        super.onCleared()
    }

    companion object {
        private const val TAG = "NearbyPlacesViewModel"

        const val SORT_RATING = 0
        const val SORT_NAME = 1
        const val SORT_OPEN_CLOSED = 2
    }
}