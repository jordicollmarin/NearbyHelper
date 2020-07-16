package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlaceDetailBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class NearbyPlaceDetailFragment : DaggerFragment() {
    lateinit var binding: FragmentNearbyPlaceDetailBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }
}