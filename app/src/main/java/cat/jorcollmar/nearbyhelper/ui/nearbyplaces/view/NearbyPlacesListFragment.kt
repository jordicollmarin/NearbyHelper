package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.jorcollmar.nearbyhelper.databinding.FragmentNearbyPlacesListBinding

class NearbyPlacesListFragment : Fragment() {
    lateinit var binding: FragmentNearbyPlacesListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPlacesListBinding.inflate(inflater, container, false)
        return binding.root
    }
}