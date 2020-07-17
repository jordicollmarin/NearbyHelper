package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.common.extension.observe
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlacesListBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
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

        observe(viewModel.places, {
            binding.prbNearbyPlaces.visibility = View.GONE
            it?.let {
                placesAdapter.updateItems(it)
            } ?: run {
                // TODO: Show error to user to retry WS call
            }
        })
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        inflater.inflate(R.menu.menu_main, menu)
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

    private fun onFilterClicked(item: MenuItem, filterType: String?): Boolean {
        item.isChecked = item.isChecked.not()
        viewModel.selectedPlaceType = filterType
        return false
    }

    private fun onOrderByClicked(): Boolean {
        AlertDialog.Builder(requireContext(), R.style.Theme_MaterialComponents_Light_Dialog)
            .setTitle(getString(R.string.nearby_places_list_order_by))
            .setSingleChoiceItems(
                resources.getStringArray(R.array.nearby_places_list_order_options),
                viewModel.selectedSortingOption
            ) { dialog, which ->
                dialog.dismiss()
                viewModel.selectedSortingOption = which
                viewModel.sortList()
            }
            .create().show()
        return false
    }

    private fun openPlaceDetail(place: Place) {
        viewModel.selectedPlace = place
        findNavController().navigate(NearbyPlacesListFragmentDirections.actionNearbyPlacesListFragmentToNearbyPlaceDetailFragment())
    }

    companion object {
        const val FILTER_BARS = "bar"
        const val FILTER_CAFES = "cafe"
        const val FILTER_RESTAURANTS = "restaurant"
    }
}