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

class GetNearbyPlacesTest : BaseUseCaseTest() {
    private val nearbyPlacesRepository: NearbyPlacesRepositoryContract = mockk(relaxed = true)
    private lateinit var getNearbyPlaces: GetNearbyPlaces

    private val passedLatitude = "latitude"
    private val passedLongitude = "longitude"
    private val passedPlaceType = "placeType"

    @Before
    fun setUpTest() {
        getNearbyPlaces = GetNearbyPlaces(schedulersFacade, nearbyPlacesRepository)
    }

    @Test
    fun `Given GetNearbyPlaces execution, When latitude, longitude and placeType are passed, Then GetNearbyPlaces from repository is invoked with passed latitude, longitude and placeType`() {
        val placeDomainListMock: List<PlaceDomain> = mockk()

        every {
            nearbyPlacesRepository.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(
            placeDomainListMock
        ).toObservable()

        captureResultForUseCase(
            observableUseCase = getNearbyPlaces,
            params = GetNearbyPlaces.Params(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        )

        verify {
            nearbyPlacesRepository.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
    }

    @Test
    fun `Given GetNearbyPlaces execution, When list of places is returned by the repository, Then same list is returned by the useCase`() {
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
            nearbyPlacesRepository.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(placeDomainList.toList()).toObservable()

        assertEquals(
            placeDomainList.toList(),
            captureResultForUseCase(
                observableUseCase = getNearbyPlaces,
                params = GetNearbyPlaces.Params(
                    passedLatitude,
                    passedLongitude,
                    passedPlaceType
                )
            )
        )
    }

    @Test
    fun `Given GetNearbyPlaces execution, When error is returned by the repository, Then same error is returned by the useCase`() {
        val throwable = Throwable("GetNearbyPlacesThrowable")

        every {
            nearbyPlacesRepository.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Observable.error(throwable)

        assertEquals(
            throwable,
            captureErrorForUseCase(
                observableUseCase = getNearbyPlaces,
                params = GetNearbyPlaces.Params(
                    passedLatitude,
                    passedLongitude,
                    passedPlaceType
                )
            )
        )
    }
}