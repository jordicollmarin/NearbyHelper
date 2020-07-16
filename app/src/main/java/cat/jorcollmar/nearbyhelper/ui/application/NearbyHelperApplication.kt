package cat.jorcollmar.nearbyhelper.ui.application

import android.app.Application
import cat.jorcollmar.nearbyhelper.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class NearbyHelperApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = androidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.create().inject(this)
    }
}