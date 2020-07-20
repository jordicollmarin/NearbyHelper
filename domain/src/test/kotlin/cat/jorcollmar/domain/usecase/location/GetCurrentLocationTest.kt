package cat.jorcollmar.domain.usecase.location

import cat.jorcollmar.domain.BaseUseCaseTest
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.repository.LocationRepositoryContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCurrentLocationTest : BaseUseCaseTest() {
    private val locationRepository: LocationRepositoryContract = mockk(relaxed = true)
    private lateinit var getCurrentLocation: GetCurrentLocation

    @Before
    fun setUpTest() {
        getCurrentLocation = GetCurrentLocation(schedulersFacade, locationRepository)
    }

    @Test
    fun `Given GetCurrentLocation execution, Then getCurrentLocation is invoked from repository`() {
        val locationDomainMock: LocationDomain = mockk()

        every { locationRepository.getCurrentLocation() } returns Single.just(locationDomainMock)

        captureResultForUseCase(
            singleUseCase = getCurrentLocation,
            params = GetCurrentLocation.Params()
        )

        verify { locationRepository.getCurrentLocation() }
    }

    @Test
    fun `Given GetCurrentLocation execution, When location is returned by the repository, Then same location is returned by the useCase`() {
        val locationDomain = LocationDomain(0.0, 0.0)

        every { locationRepository.getCurrentLocation() } returns Single.just(locationDomain)

        assertEquals(
            locationDomain,
            captureResultForUseCase(
                singleUseCase = getCurrentLocation,
                params = GetCurrentLocation.Params()
            )
        )
    }

    @Test
    fun `Given GetCurrentLocation execution, When error is returned by the repository, Then same error is returned by the useCase`() {
        val throwable = Throwable("GetCurrentLocationThrowable")

        every { locationRepository.getCurrentLocation() } returns Single.error(throwable)

        assertEquals(
            throwable,
            captureErrorForUseCase(
                singleUseCase = getCurrentLocation,
                params = GetCurrentLocation.Params()
            )
        )
    }
}