package cat.jorcollmar.domain.usecase.nearbyplaces

import cat.jorcollmar.domain.BaseUseCaseTest
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.model.PlaceDomain
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNearbyPlacesOrderByDistanceTest : BaseUseCaseTest() {
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract = mockk(relaxed = true)
    private lateinit var getNearbyPlacesOrderedByDistance: GetNearbyPlacesOrderedByDistance

    private val passedLatitude = "latitude"
    private val passedLongitude = "longitude"
    private val passedPlaceType = "placeType"

    @Before
    fun setUpTest() {
        getNearbyPlacesOrderedByDistance =
            GetNearbyPlacesOrderedByDistance(schedulersFacade, nearbyPlacesRepository)
    }

    @Test
    fun `Given GetNearbyPlacesOrderedByDistance execution, When latitude, longitude and placeType are passed, Then GetNearbyPlacesOrderedByDistance from repository is invoked with passed latitude, longitude and placeType`() {
        val placeDomainListMock: List<PlaceDomain> = mockk()

        every {
            nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(
            placeDomainListMock
        ).toObservable()

        captureResultForUseCase(
            observableUseCase = getNearbyPlacesOrderedByDistance,
            params = GetNearbyPlacesOrderedByDistance.Params(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        )

        verify {
            nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
    }

    @Test
    fun `Given GetNearbyPlacesOrderedByDistance execution, When list of places is returned by the repository, Then same list is returned by the useCase`() {
        val placeDomainList: MutableList<PlaceDomain> = mutableListOf()
        placeDomainList.add(
            PlaceDomain(
                "id", "",
                "", 0, "",
                0.0, 0, true,
                LocationDomain(0.0, 0.0), listOf()
            )
        )
        every {
            nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(placeDomainList.toList()).toObservable()

        assertEquals(
            placeDomainList.toList(),
            captureResultForUseCase(
                observableUseCase = getNearbyPlacesOrderedByDistance,
                params = GetNearbyPlacesOrderedByDistance.Params(
                    passedLatitude,
                    passedLongitude,
                    passedPlaceType
                )
            )
        )
    }

    @Test
    fun `Given GetNearbyPlacesOrderedByDistance execution, When error is returned by the repository, Then same error is returned by the useCase`() {
        val throwable = Throwable("GetNearbyPlacesOrderedByDistanceThrowable")

        every {
            nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Observable.error(throwable)

        assertEquals(
            throwable,
            captureErrorForUseCase(
                observableUseCase = getNearbyPlacesOrderedByDistance,
                params = GetNearbyPlacesOrderedByDistance.Params(
                    passedLatitude,
                    passedLongitude,
                    passedPlaceType
                )
            )
        )
    }
}