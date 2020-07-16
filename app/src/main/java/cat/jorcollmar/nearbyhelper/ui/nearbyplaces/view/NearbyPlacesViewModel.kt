package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.jorcollmar.domain.usecase.nearbyplaces.GetAllNearbyPlaces
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import io.reactivex.functions.Consumer
import javax.inject.Inject

class NearbyPlacesViewModel @Inject constructor(
    private val getAllNearbyPlaces: GetAllNearbyPlaces,
    private val placeMapper: PlaceMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>>
        get() = _places

    private lateinit var _selectedPlace: Place

    init {
        getNearbyPlacesList()
    }

    fun setSelectedPlace(place: Place) {
        _selectedPlace = place
    }

    fun getNearbyPlacesList() {
        _loading.value = true

        getAllNearbyPlaces.execute(
            Consumer {
                _loading.value = false
                _places.value = placeMapper.map(it)
            },
            Consumer {
                Log.e(TAG, it.localizedMessage ?: it.message ?: "Could not get nearby places")
                _loading.value = false
                _places.value = null
            },
            // TODO: Get latitude and longitude from viewModelVariable currentPosition
            GetAllNearbyPlaces.Params("-33.8670522", "151.1957362")
        )
    }

    override fun onCleared() {
        getAllNearbyPlaces.dispose()
        super.onCleared()
    }

    companion object {
        private const val TAG = "NearbyPlacesViewModel"
    }
}