package cat.jorcollmar.nearbyhelper.di

import android.content.Context
import cat.jorcollmar.data.repository.LocationRepository
import cat.jorcollmar.data.repository.NearbyPlacesRepository
import cat.jorcollmar.data.source.GooglePlacesWebservice
import cat.jorcollmar.domain.repository.LocationRepositoryContract
import cat.jorcollmar.domain.repository.NearbyPlacesRepositoryContract
import cat.jorcollmar.nearbyhelper.BuildConfig
import cat.jorcollmar.nearbyhelper.ui.NearbyHelperActivity
import cat.jorcollmar.nearbyhelper.ui.application.NearbyHelperApplication
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlaceDetailFragment
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesListFragment
import cat.jorcollmar.domain.common.SchedulersFacade
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class NearbyHelperModule {

    @ContributesAndroidInjector
    abstract fun contributeNearbyHelperActivity(): NearbyHelperActivity

    @ContributesAndroidInjector
    abstract fun contributeNearbyPlacesListFragment(): NearbyPlacesListFragment

    @ContributesAndroidInjector
    abstract fun contributeNearbyPlaceDetailFragment(): NearbyPlaceDetailFragment

    @Binds
    abstract fun bindNearbyPlacesRepository(repository: NearbyPlacesRepository): NearbyPlacesRepositoryContract

    @Binds
    abstract fun bindLocationRepository(repository: LocationRepository): LocationRepositoryContract

    @Module
    companion object {

        @Provides
        fun provideContext(nearbyHelper: NearbyHelperApplication): Context {
            return nearbyHelper
        }

        @Provides
        @Singleton
        fun provideDomainSchedulersFacade(): SchedulersFacade {
            return object : SchedulersFacade {
                override fun getIo(): Scheduler {
                    return Schedulers.io()
                }

                override fun getNewThread(): Scheduler {
                    return Schedulers.newThread()
                }

                override fun getAndroidMainThread(): Scheduler {
                    return AndroidSchedulers.mainThread()
                }
            }
        }

        @JvmStatic
        @Provides
        fun provideGooglePlacesWebservice(): GooglePlacesWebservice {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.GOOGLE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GooglePlacesWebservice::class.java)
        }
    }

}