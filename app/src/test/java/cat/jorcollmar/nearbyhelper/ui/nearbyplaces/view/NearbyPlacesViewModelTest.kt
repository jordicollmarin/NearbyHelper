package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.jorcollmar.domain.usecase.location.GetCurrentLocation
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaceDetail
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlacesOrderedByDistance
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.LocationMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NearbyPlacesViewModelTest {
    private val getCurrentLocation: GetCurrentLocation = mockk(relaxed = true)
    private val locationMapper: LocationMapper = mockk(relaxed = true)
    private val getNearbyPlaces: GetNearbyPlaces = mockk(relaxed = true)
    private val getNearbyPlaceDetail: GetNearbyPlaceDetail = mockk(relaxed = true)
    private val getNearbyPlacesOrderedByDistance: GetNearbyPlacesOrderedByDistance =
        mockk(relaxed = true)
    private val placeMapper: PlaceMapper = mockk(relaxed = true)

    private lateinit var viewModel: NearbyPlacesViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = NearbyPlacesViewModel(
            getCurrentLocation,
            locationMapper,
            getNearbyPlaces,
            getNearbyPlaceDetail,
            getNearbyPlacesOrderedByDistance,
            placeMapper
        )
    }

    @Test
    fun `When a selected placeType is selected, Then value is saved correctly and getNearbyPlaces or getNearbyPlacesOrderedByDistance is executed`() {
        val placeType = "bar"

        viewModel.selectedPlaceType = placeType


    }
}