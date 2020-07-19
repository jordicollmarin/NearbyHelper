package cat.jorcollmar.data.repository

import cat.jorcollmar.data.mapper.LocationDataMapper
import cat.jorcollmar.data.model.LocationData
import cat.jorcollmar.data.source.googlefusedlocation.GoogleFusedLocationApiDataSource
import cat.jorcollmar.domain.model.LocationDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {
    private val googleFusedLocationApiDataSource: GoogleFusedLocationApiDataSource =
        mockk(relaxed = true)
    private val locationDataMapper: LocationDataMapper = mockk(relaxed = true)

    private lateinit var locationRepository: LocationRepository

    @Before
    fun setUp() {
        locationRepository = LocationRepository(
            googleFusedLocationApiDataSource,
            locationDataMapper
        )
    }

    @Test
    fun `When getCurrentLocation is called And locationObservable is assigned with the current location, Then return it`() {
        val locationData = LocationData(0.0, 0.0)
        val locationDomain = LocationDomain(0.0, 0.0)

        every { googleFusedLocationApiDataSource.locationObservable } returns Single.just(
            locationData
        )
        every { locationDataMapper.map(any<LocationData>()) } returns locationDomain

        val observable = locationRepository.getCurrentLocation()
        val testObserver = TestObserver<LocationDomain>()
        observable.subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        verify { googleFusedLocationApiDataSource.locationObservable }
        Assert.assertEquals(locationDomain, testObserver.values()[0])
    }

    @Test
    fun `When getCurrentLocation is called And locationObservable is assigned with an error, Then the error is returned `() {
        val error = Throwable("LocationProblem")

        every { googleFusedLocationApiDataSource.locationObservable } returns Single.error(error)

        val observable = locationRepository.getCurrentLocation()
        val testObserver = TestObserver<LocationDomain>()
        observable.subscribe(testObserver)

        testObserver.assertNotComplete()
        testObserver.assertError(error)
        verify { googleFusedLocationApiDataSource.locationObservable }
    }
}