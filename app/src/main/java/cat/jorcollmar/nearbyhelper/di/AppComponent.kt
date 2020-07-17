package cat.jorcollmar.nearbyhelper.di

import cat.jorcollmar.nearbyhelper.ui.application.NearbyHelperApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, NearbyHelperModule::class])
interface AppComponent : AndroidInjector<NearbyHelperApplication> {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(nearbyHelperApplication: NearbyHelperApplication): Builder
    }
}