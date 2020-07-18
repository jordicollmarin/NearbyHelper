package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.commons.extensions.observe
import cat.jorcollmar.nearbyhelper.commons.factories.AlertDialogFactory
import cat.jorcollmar.nearbyhelper.commons.managers.PermissionManager
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlacesListBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_LOCATION
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_NEARBY_PLACES
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_NEARBY_PLACES_DISTANCE
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_PERMISSION_DENIED
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.adapter.NearbyPlacesAdapter
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class NearbyPlacesListFragment : DaggerFragment() {
    lateinit var binding: FragmentNearbyPlacesListBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var viewModelFactory: NearbyPlacesViewModelFactory

    private val viewModel: NearbyPlacesViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(NearbyPlacesViewModel::class.java)
    }

    private val placesAdapter: NearbyPlacesAdapter by lazy {
        NearbyPlacesAdapter(::openPlaceDetail)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requestLocationPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionResult(requestCode, grantResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvNearbyPlaces.layoutManager = LinearLayoutManager(context)
        binding.rcvNearbyPlaces.adapter = placesAdapter
        initObservers()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        viewModel.selectedPlaceType?.let {
            when (it) {
                FILTER_BARS -> menu.findItem(R.id.action_filter_bars).setChecked(true)
                FILTER_CAFES -> menu.findItem(R.id.action_filter_cafes).setChecked(true)
                FILTER_RESTAURANTS -> menu.findItem(R.id.action_filter_restaurants).setChecked(true)
                else -> menu.findItem(R.id.action_filter_all).setChecked(true)
            }
        } ?: run {
            menu.findItem(R.id.action_filter_all).setChecked(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_all -> onFilterClicked(item, null)
            R.id.action_filter_bars -> onFilterClicked(item, FILTER_BARS)
            R.id.action_filter_cafes -> onFilterClicked(item, FILTER_CAFES)
            R.id.action_filter_restaurants -> onFilterClicked(item, FILTER_RESTAURANTS)
            R.id.action_order_by -> onOrderByClicked()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestLocationPermission() {
        if (permissionManager.isLocationPermissionGranted(requireContext())) {
            viewModel.onLocationPermissionGranted()
        } else {
            permissionManager.requestLocationPermission(this)
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.observe(viewModel.loading, {
            it?.let {
                if (it) {
                    binding.prbNearbyPlaces.visibility = View.VISIBLE
                    binding.rcvNearbyPlaces.visibility = View.GONE
                } else {
                    binding.prbNearbyPlaces.visibility = View.GONE
                    binding.rcvNearbyPlaces.visibility = View.VISIBLE
                }
            } ?: run {
                binding.prbNearbyPlaces.visibility = View.GONE
            }
        })

        viewLifecycleOwner.observe(viewModel.places, {
            binding.prbNearbyPlaces.visibility = View.GONE
            it?.let {
                if (it.isEmpty()) {
                    binding.txvNearbyPlacesEmptyList.visibility = View.VISIBLE
                    binding.rcvNearbyPlaces.visibility = View.GONE
                } else {
                    binding.txvNearbyPlacesEmptyList.visibility = View.GONE
                    binding.rcvNearbyPlaces.visibility = View.VISIBLE
                    placesAdapter.updateItems(it, viewModel.currentLocation)
                }
            }
        })

        viewLifecycleOwner.observe(viewModel.error, {
            it?.let {
                when (it) {
                    ERROR_PERMISSION_DENIED -> showPermissionDeniedDialog()
                    ERROR_LOCATION -> showLocationErrorDialog()
                    ERROR_NEARBY_PLACES -> showNearbyPlacesListErrorDialog()
                    ERROR_NEARBY_PLACES_DISTANCE -> showNearbyPlacesListErrorDialog()
                }
            }
        })
    }

    private fun showNearbyPlacesListErrorDialog() {
        AlertDialogFactory.createAlertDialog(
            requireContext(),
            false,
            getString(R.string.error_nearby_places_error_message),
            getString(R.string.button_retry)
        ) { dialog ->
            dialog.dismiss()
            viewModel.getNearbyPlacesList()
        }.show()
    }

    private fun showLocationErrorDialog() {
        AlertDialogFactory.createAlertDialog(
            requireContext(),
            false,
            getString(R.string.error_location_unknown_message),
            getString(R.string.button_retry)
        ) { dialog ->
            dialog.dismiss()
            viewModel.getCurrentLocation()
        }.show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialogFactory.createAlertDialog(
            requireContext(),
            false,
            getString(R.string.error_location_permission_denied_message),
            getString(R.string.button_ok)
        ) { dialog ->
            dialog.dismiss()
            activity?.finish()
        }.show()
    }

    private fun onFilterClicked(item: MenuItem, filterType: String?): Boolean {
        item.isChecked = item.isChecked.not()
        viewModel.selectedPlaceType = filterType
        return false
    }

    private fun onOrderByClicked(): Boolean {
        AlertDialogFactory.createSingleChoiceAlertDialog(
            requireContext(),
            getString(R.string.nearby_places_list_order_by),
            resources.getStringArray(R.array.nearby_places_list_order_options),
            viewModel.selectedSortingOption
        ) { dialog, which ->
            dialog.dismiss()
            viewModel.selectedSortingOption = which
            viewModel.sortList()
        }.show()

        return false
    }

    private fun openPlaceDetail(place: Place) {
        viewModel.selectedPlaceId = place.id
        findNavController().navigate(NearbyPlacesListFragmentDirections.actionNearbyPlacesListFragmentToNearbyPlaceDetailFragment())
    }

    companion object {
        const val FILTER_BARS = "bar"
        const val FILTER_CAFES = "cafe"
        const val FILTER_RESTAURANTS = "restaurant"
    }
}