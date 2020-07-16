package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.jorcollmar.domain.usecase.nearbyplaces.GetAllNearbyPlaces
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import javax.inject.Inject

class NearbyPlacesViewModelFactory @Inject constructor(
    private val getAllNearbyPlaces: GetAllNearbyPlaces,
    private val placeMapper: PlaceMapper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearbyPlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NearbyPlacesViewModel(getAllNearbyPlaces, placeMapper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
