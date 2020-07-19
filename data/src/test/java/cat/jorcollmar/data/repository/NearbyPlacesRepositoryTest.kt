package cat.jorcollmar.data.repository

import cat.jorcollmar.data.mapper.PlaceDataMapper
import cat.jorcollmar.data.model.LocationData
import cat.jorcollmar.data.model.PlaceData
import cat.jorcollmar.data.source.googleplaces.GooglePlacesApiDataSource
import cat.jorcollmar.domain.model.LocationDomain
import cat.jorcollmar.domain.model.PlaceDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NearbyPlacesRepositoryTest {

    private val googlePlacesApiDataSource: GooglePlacesApiDataSource = mockk(relaxed = true)
    private val placeDataMapper: PlaceDataMapper = mockk(relaxed = true)

    private lateinit var nearbyPlacesRepository: NearbyPlacesRepository

    private val passedLatitude = "latitude"
    private val passedLongitude = "longitude"
    private val passedPlaceType = "placeType"

    private val placesData: List<PlaceData> = mutableListOf<PlaceData>().apply {
        add(
            PlaceData(
                "id", "",
                "", 0, "",
                0.0, 0, true,
                LocationData(0.0, 0.0), listOf()
            )
        )
    }

    private val placesDomain: List<PlaceDomain> = mutableListOf<PlaceDomain>().apply {
        add(
            PlaceDomain(
                "id", "",
                "", 0, "",
                0.0, 0, true,
                LocationDomain(0.0, 0.0), listOf()
            )
        )
    }

    @Before
    fun setUp() {
        nearbyPlacesRepository = NearbyPlacesRepository(
            googlePlacesApiDataSource,
            placeDataMapper
        )
    }

    @Test
    fun `When getNearbyPlaces is called And getNearbyPlaces from repository returns a list of places, Then return it`() {
        every {
            googlePlacesApiDataSource.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(
            placesData
        ).toObservable()
        every { placeDataMapper.map(any<List<PlaceData>>()) } returns placesDomain

        val observable = nearbyPlacesRepository.getNearbyPlaces(
            passedLatitude,
            passedLongitude,
            passedPlaceType
        )
        val testObserver = TestObserver<List<PlaceDomain>>()
        observable.subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        verify {
            googlePlacesApiDataSource.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
        Assert.assertEquals(placesDomain, testObserver.values()[0])
    }

    @Test
    fun `When getNearbyPlaces is called And getNearbyPlaces from repository returns an error, Then the error is returned `() {
        val error = Throwable("NearbyPlacesThrowable")

        every {
            googlePlacesApiDataSource.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Observable.error(error)

        val observable = nearbyPlacesRepository.getNearbyPlaces(
            passedLatitude,
            passedLongitude,
            passedPlaceType
        )
        val testObserver = TestObserver<List<PlaceDomain>>()
        observable.subscribe(testObserver)

        testObserver.assertNotComplete()
        testObserver.assertError(error)
        verify {
            googlePlacesApiDataSource.getNearbyPlaces(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
    }

    @Test
    fun `When getNearbyPlacesOrderedByDistance is called And getNearbyPlacesOrderedByDistance from repository returns a list of places, Then return it`() {
        every {
            googlePlacesApiDataSource.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Single.just(
            placesData
        ).toObservable()
        every { placeDataMapper.map(any<List<PlaceData>>()) } returns placesDomain

        val observable = nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
            passedLatitude,
            passedLongitude,
            passedPlaceType
        )
        val testObserver = TestObserver<List<PlaceDomain>>()
        observable.subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        verify {
            googlePlacesApiDataSource.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
        Assert.assertEquals(placesDomain, testObserver.values()[0])
    }

    @Test
    fun `When getNearbyPlacesOrderedByDistance is called And getNearbyPlacesOrderedByDistance from repository returns an error, Then the error is returned `() {
        val error = Throwable("NearbyPlacesOrderedByDistanceThrowable")

        every {
            googlePlacesApiDataSource.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        } returns Observable.error(error)

        val observable = nearbyPlacesRepository.getNearbyPlacesOrderedByDistance(
            passedLatitude,
            passedLongitude,
            passedPlaceType
        )
        val testObserver = TestObserver<List<PlaceDomain>>()
        observable.subscribe(testObserver)

        testObserver.assertNotComplete()
        testObserver.assertError(error)
        verify {
            googlePlacesApiDataSource.getNearbyPlacesOrderedByDistance(
                passedLatitude,
                passedLongitude,
                passedPlaceType
            )
        }
    }

    @Test
    fun `When getNearbyDetail is called And getNearbyPlaceDetail from repository returns a place, Then return it`() {
        val placeId = "placeId"
        val placeData = PlaceData(
            "id", "",
            "", 0, "",
            0.0, 0, true,
            LocationData(0.0, 0.0), listOf()
        )

        val placeDomain = PlaceDomain(
            "id", "",
            "", 0, "",
            0.0, 0, true,
            LocationDomain(0.0, 0.0), listOf()
        )

        every { googlePlacesApiDataSource.getNearbyPlaceDetail(placeId) } returns Single.just(
            placeData
        )
        every { placeDataMapper.map(any<PlaceData>()) } returns placeDomain

        val observable = nearbyPlacesRepository.getNearbyDetail(placeId)
        val testObserver = TestObserver<PlaceDomain>()
        observable.subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        verify { googlePlacesApiDataSource.getNearbyPlaceDetail(placeId) }
        Assert.assertEquals(placeDomain, testObserver.values()[0])
    }

    @Test
    fun `When getNearbyDetail is called And getNearbyPlaceDetail from repository returns an error, Then the error is returned `() {
        val placeId = "placeId"
        val error = Throwable("NearbyDetailThrowable")

        val placeDomain = PlaceDomain(
            "id", "",
            "", 0, "",
            0.0, 0, true,
            LocationDomain(0.0, 0.0), listOf()
        )

        every {
            googlePlacesApiDataSource.getNearbyPlaceDetail(placeId)
        } returns Single.error(error)

        val observable = nearbyPlacesRepository.getNearbyDetail(placeId)
        val testObserver = TestObserver<PlaceDomain>()
        observable.subscribe(testObserver)

        testObserver.assertNotComplete()
        testObserver.assertError(error)
        verify { googlePlacesApiDataSource.getNearbyPlaceDetail(placeId) }
    }
}