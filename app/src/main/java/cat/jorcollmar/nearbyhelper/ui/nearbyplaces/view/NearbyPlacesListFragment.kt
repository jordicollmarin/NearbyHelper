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
import cat.jorcollmar.nearbyhelper.common.extension.observe
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlacesListBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.adapter.NearbyPlaceListAdapter
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

    private val placesAdapter: NearbyPlaceListAdapter by lazy {
        NearbyPlaceListAdapter(::openPlaceDetail)
    }

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
        binding = FragmentNearbyPlacesListBinding.inflate(inflater, container, false)

        binding.rcvNearbyPlaces.layoutManager = LinearLayoutManager(context)
        binding.rcvNearbyPlaces.adapter = placesAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.observe(viewModel.loading, {
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

        viewLifecycleOwner.observe(viewModel.places, {
            binding.prbNearbyPlaces.visibility = View.GONE
            it?.let {
                placesAdapter.updateItems(it)
            } ?: run {
                // TODO: Show error to user to retry WS call
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_all -> onFilterClicked(item, FILTER_ALL)
            R.id.action_filter_bars -> onFilterClicked(item, FILTER_BARS)
            R.id.action_filter_cafes -> onFilterClicked(item, FILTER_CAFES)
            R.id.action_filter_restaurants -> onFilterClicked(item, FILTER_RESTAURANTS)
            R.id.action_order_by -> onOrderByClicked()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFilterClicked(item: MenuItem, filterType: Int): Boolean {
        item.isChecked = item.isChecked.not()

        when (filterType) {
            FILTER_ALL -> viewModel.getNearbyPlacesList()
            FILTER_BARS -> {
            }
            FILTER_CAFES -> {
            }
            FILTER_RESTAURANTS -> {
            }
        }

        return false
    }

    private fun onOrderByClicked(): Boolean {
        // TODO: Open Order by (Name, Open/Closed, etc.)
        return false
    }

    private fun openPlaceDetail(place: Place) {
        viewModel.setSelectedPlace(place)
        findNavController().navigate(NearbyPlacesListFragmentDirections.actionNearbyPlacesListFragmentToNearbyPlaceDetailFragment())
    }

    companion object {
        const val FILTER_ALL = 0
        const val FILTER_BARS = 1
        const val FILTER_CAFES = 2
        const val FILTER_RESTAURANTS = 3
    }
}