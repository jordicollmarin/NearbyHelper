package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.BaseUseCaseTest
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNearbyPlaceDetailTest : BaseUseCaseTest() {
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract = mockk(relaxed = true)
    private lateinit var getNearbyPlaceDetail: GetNearbyPlaceDetail

    @Before
    fun setUpTest() {
        getNearbyPlaceDetail = GetNearbyPlaceDetail(schedulersFacade, nearbyPlacesRepository)
    }

    @Test
    fun `Given GetNearbyPlaceDetail execution, When place id is passed, Then getNearbyDetail from repository is invoked with passed id`() {
        val passedId = "placeId"
        val placeDomainMock: PlaceDomain = mockk()

        every { nearbyPlacesRepository.getNearbyDetail(passedId) } returns Single.just(
            placeDomainMock
        )

        captureResultForUseCase(
            singleUseCase = getNearbyPlaceDetail,
            params = GetNearbyPlaceDetail.Params(passedId)
        )

        verify { nearbyPlacesRepository.getNearbyDetail(passedId) }
    }

    @Test
    fun `Given GetNearbyPlaceDetail execution, When place is returned by the repository, Then same place is returned by the useCase`() {
        val passedId = "placeId"
        val locationDomain = LocationDomain(0.0, 0.0)
        val placeDomain =
            PlaceDomain(passedId, "", "", 0, "", 0.0, 0, true, locationDomain, listOf())

        every { nearbyPlacesRepository.getNearbyDetail(passedId) } returns Single.just(placeDomain)

        assertEquals(
            placeDomain,
            captureResultForUseCase(
                singleUseCase = getNearbyPlaceDetail,
                params = GetNearbyPlaceDetail.Params(passedId)
            )
        )
    }

    @Test
    fun `Given GetNearbyPlaceDetail execution, When error is returned by the repository, Then same error is returned by the useCase`() {
        val passedId = "placeId"
        val throwable = Throwable("GetNearbyPlaceDetailThrowable")

        every { nearbyPlacesRepository.getNearbyDetail(passedId) } returns Single.error(throwable)

        assertEquals(
            throwable,
            captureErrorForUseCase(
                singleUseCase = getNearbyPlaceDetail,
                params = GetNearbyPlaceDetail.Params(passedId)
            )
        )
    }
}