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
    private val locationRepositoryContract: LocationRepositoryContract = mockk(relaxed = true)
    private lateinit var getCurrentLocation: GetCurrentLocation

    @Before
    fun setUpTest() {
        getCurrentLocation = GetCurrentLocation(schedulersFacade, locationRepositoryContract)
    }

    @Test
    fun `Given GetCurrentLocation execution, Then getCurrentLocation is executed from repository`() {
        val locationDomainMock: LocationDomain = mockk()

        every { locationRepositoryContract.getCurrentLocation() } returns Single.just(
            locationDomainMock
        )

        captureResultForUseCase(
            singleUseCase = getCurrentLocation,
            params = GetCurrentLocation.Params()
        )

        verify { locationRepositoryContract.getCurrentLocation() }
    }

    @Test
    fun `Given location returned by the repository, Then same location is returned by the useCase`() {
        val locationDomain = LocationDomain(0.0, 0.0)

        every { locationRepositoryContract.getCurrentLocation() } returns Single.just(locationDomain)

        assertEquals(
            locationDomain,
            captureResultForUseCase(
                singleUseCase = getCurrentLocation,
                params = GetCurrentLocation.Params()
            )
        )
    }

    @Test
    fun `Given error returned by the repository, Then same error is returned by the useCase`() {
        val throwable = Throwable("GetCurrentLocationThrowable")

        every { locationRepositoryContract.getCurrentLocation() } returns Single.error(throwable)

        assertEquals(
            throwable,
            captureErrorForUseCase(
                singleUseCase = getCurrentLocation,
                params = GetCurrentLocation.Params()
            )
        )
    }
}