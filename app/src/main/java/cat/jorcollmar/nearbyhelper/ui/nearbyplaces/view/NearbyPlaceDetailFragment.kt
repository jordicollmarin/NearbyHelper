package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.commons.extensions.observe
import cat.jorcollmar.nearbyhelper.commons.extensions.round
import cat.jorcollmar.nearbyhelper.commons.factories.AlertDialogFactory
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlaceDetailBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.NearbyPlacesViewModel.Companion.ERROR_NEARBY_PLACE_DETAIL
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.adapter.NearbyPlacesImagesAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.math.abs

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

    private val imagesAdapter: NearbyPlacesImagesAdapter by lazy {
        NearbyPlacesImagesAdapter()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        initObservers()
        setUpLayout()
    }

    private fun setUpLayout() {
        binding.vwpNearbyPlaceDetail.adapter = imagesAdapter
        binding.vwpNearbyPlaceDetail.offscreenPageLimit = 2
        binding.fabNearbyPlaceDetail.setOnClickListener { callToPlace() }
        binding.ablNearbyPlaceDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener
        { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                binding.toolbarNearbyPlaceDetail.menu.findItem(R.id.action_call).isVisible = true
            } else {
                binding.toolbarNearbyPlaceDetail.menu.findItem(R.id.action_call).isVisible = false
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarNearbyPlaceDetail)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_call).isChecked = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.action_call -> callToPlace()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callToPlace(): Boolean {
        viewModel.selectedPlace.value?.internationalPhoneNumber?.let {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null)))
        } ?: run {
            Snackbar.make(
                binding.root,
                getString(R.string.nearby_place_detail_phone_not_available),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    private fun initObservers() {
        viewLifecycleOwner.observe(viewModel.loading, {
            it?.let {
                if (it) {
                    binding.prbNearbyPlaceDetail.visibility = View.VISIBLE
                    binding.lytContent.scvContentNearbyPlaceDetail.visibility = View.GONE
                    binding.ablNearbyPlaceDetail.visibility = View.GONE
                    binding.fabNearbyPlaceDetail.visibility = View.GONE
                } else {
                    binding.prbNearbyPlaceDetail.visibility = View.GONE
                    binding.lytContent.scvContentNearbyPlaceDetail.visibility = View.VISIBLE
                    binding.ablNearbyPlaceDetail.visibility = View.VISIBLE
                    binding.fabNearbyPlaceDetail.visibility = View.VISIBLE
                }
            } ?: run {
                binding.prbNearbyPlaceDetail.visibility = View.GONE
            }
        })

        viewLifecycleOwner.observe(viewModel.selectedPlace, {
            it?.let { place ->
                loadNearbyPlaceData(place)
            }
        })

        viewLifecycleOwner.observe(viewModel.error, {
            it?.let {
                when (it) {
                    ERROR_NEARBY_PLACE_DETAIL -> showDetailInfoErrorDialog()
                }
            }
        })
    }

    private fun loadNearbyPlaceData(place: Place) {
        with(place) {
            binding.ctlNearbyPlaceDetail.title = name

            photos?.let { photos ->
                imagesAdapter.updateItems(photos.map { it.getPhotoUri() })
            } ?: run {
                imagesAdapter.setEmptyState()
            }

            rating?.let {
                binding.lytContent.txvNearbyPlaceDetailRating.visibility = View.VISIBLE
                binding.lytContent.txvNearbyPlaceDetailRating.text = context?.getString(
                    R.string.nearby_places_list_item_rating,
                    place.getRatingFormatted()
                )
            } ?: run {
                binding.lytContent.txvNearbyPlaceDetailRating.visibility = View.GONE
            }

            location?.let {
                binding.lytContent.txvNearbyPlaceDetailDistance.visibility = View.VISIBLE
                binding.lytContent.txvNearbyPlaceDetailDistance.text = context?.getString(
                    R.string.nearby_places_list_item_distance,
                    it.getDistance(viewModel.currentLocation)?.round(2)
                        ?: run { context?.getString(R.string.nearby_places_list_item_distance_unknown) }
                )
            } ?: run {
                binding.lytContent.txvNearbyPlaceDetailDistance.visibility = View.GONE
            }

            priceLevel?.let {
                binding.lytContent.txvNearbyPlaceDetailPriceLevel.visibility = View.GONE
                binding.lytContent.txvNearbyPlaceDetailPriceLevel.text =
                    context?.getString(R.string.nearby_place_detail_price_level, it.toString())
            } ?: run {
                binding.lytContent.txvNearbyPlaceDetailPriceLevel.visibility =
                    View.VISIBLE
            }
        }
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