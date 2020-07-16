package cat.jorcollmar.nearbyhelper.ui.nearbyhelper.view

import android.os.Bundle
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.databinding.ActivityNearbyHelperBinding
import dagger.android.support.DaggerAppCompatActivity

class NearbyHelperActivity : DaggerAppCompatActivity() {
    lateinit var binding: ActivityNearbyHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Replace Splash theme with a NoActionBar theme
        setTheme(R.style.AppTheme_NoActionBar)

        super.onCreate(savedInstanceState)
        binding = ActivityNearbyHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}