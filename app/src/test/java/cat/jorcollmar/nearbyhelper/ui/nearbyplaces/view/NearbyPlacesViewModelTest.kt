package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.jorcollmar.domain.repository.LocationRepositoryContract
import cat.jorcollmar.domain.usecase.location.GetCurrentLocation
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaceDetail
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlaces
import cat.jorcollmar.domain.usecase.nearbyplaces.GetNearbyPlacesOrderedByDistance
import cat.jorcollmar.nearbyhelper.commons.managers.PermissionManager
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.LocationMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.mapper.PlaceMapper
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_PERMISSION_DENIED
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
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
    private val locationRepository: LocationRepositoryContract = mockk(relaxed = true)

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
    fun `When location permission is granted and places list is null, Then getCurrentLocation is executed`() {
        val grantResults: IntArray = intArrayOf(PERMISSION_GRANTED)

        viewModel.onRequestPermissionResult(
            PermissionManager.LOCATION_PERMISSION_REQUEST_CODE,
            grantResults
        )

        verify { getCurrentLocation.execute(any(), any(), any()) }
    }

    @Test
    fun `When location permission is not allowed, Then error is assigned with error code ERROR_PERMISSION_DENIED`() {
        val grantResults: IntArray = intArrayOf(PERMISSION_DENIED)

        viewModel.onRequestPermissionResult(
            PermissionManager.LOCATION_PERMISSION_REQUEST_CODE,
            grantResults
        )

        assertEquals(viewModel.error.value, ERROR_PERMISSION_DENIED)
    }

    @Test
    fun `When changeDetailLoaded is executed, Then _placeDetailsLoaded is assigned with value given by parameter`() {
        val isLoaded = true
        viewModel.changeDetailLoaded(isLoaded)
        assertEquals(isLoaded, viewModel.placeDetailsLoaded)
    }
}