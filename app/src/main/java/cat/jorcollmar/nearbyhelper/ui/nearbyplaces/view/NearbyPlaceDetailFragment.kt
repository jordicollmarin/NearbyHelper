package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.commons.extensions.observe
import cat.jorcollmar.nearbyhelper.commons.factories.AlertDialogFactory
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlaceDetailBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_NEARBY_PLACE_DETAIL
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class NearbyPlaceDetailFragment : DaggerFragment() {
    lateinit var binding: FragmentNearbyPlaceDetailBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: NearbyPlacesViewModelFactory

    private val viewModel: NearbyPlacesViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(NearbyPlacesViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initObservers()
        viewModel.getNearbyPlaceDetail()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            viewModel.selectedPlace.value?.name
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

    private fun initObservers() {
        observe(viewModel.loading, {
            it?.let {
                binding.prbNearbyPlaces.visibility = if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                binding.rcvNearbyPlaces.visibility = if (it) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            } ?: run {
                binding.prbNearbyPlaces.visibility = View.GONE
            }
        })

        observe(viewModel.selectedPlace, {
            it?.let { place ->
                loadNearbyPlaceData(place)
            }
        })

        observe(viewModel.error, {
            it?.let {
                when (it) {
                    ERROR_NEARBY_PLACE_DETAIL -> showDetailInfoErrorDialog()
                }
            }
        })
    }

    private fun loadNearbyPlaceData(place: Place) {
        TODO("Not yet implemented")
    }

    private fun showDetailInfoErrorDialog() {
        AlertDialogFactory.createAlertDialog(
            requireContext(),
            false,
            getString(R.string.error_nearby_place_detail_error_message),
            getString(R.string.button_retry),
            { dialog ->
                dialog.dismiss()
                viewModel.getNearbyPlaceDetail()
            },
            getString(R.string.button_go_back)
        ) { dialog ->
            dialog.dismiss()
            findNavController().navigateUp()
        }.show()
    }
}