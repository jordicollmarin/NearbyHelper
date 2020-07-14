package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlaceDetailBinding

class NearbyPlaceDetailFragment : Fragment() {
    lateinit var binding: FragmentNearbyPlaceDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}