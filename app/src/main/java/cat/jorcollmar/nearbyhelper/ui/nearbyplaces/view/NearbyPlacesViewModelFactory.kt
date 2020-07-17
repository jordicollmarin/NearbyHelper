package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.jorcollmar.domain.usecase.location.GetCurrentLocation
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.LocationMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import javax.inject.Inject

class NearbyPlacesViewModelFactory @Inject constructor(
    private val getCurrentLocation: GetCurrentLocation,
    private val locationMapper: LocationMapper,
    private val getNearbyPlaces: GetNearbyPlaces,
    private val placeMapper: PlaceMapper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearbyPlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NearbyPlacesViewModel(
                getCurrentLocation,
                locationMapper,
                getNearbyPlaces,
                placeMapper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
