package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.common.extension.loadImage
import cat.jorcollmar.nearbyhelper.databinding.NearbyPlaceItemBinding
import cat.jorcollmar.nearbyhelper.ui.nearbyplaces.model.Place

class NearbyPlacesAdapter(
    private val onPlaceClick: (Place) -> Unit
) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {

    private var placesList: MutableList<Place> = mutableListOf()

    fun updateItems(places: List<Place>) {
        placesList = places.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NearbyPlaceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onPlaceClick
        )
    }

    override fun getItemCount(): Int = placesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.load(placesList[position])
    }

    inner class ViewHolder(
        private val nearbyPlaceItemBinding: NearbyPlaceItemBinding,
        private val onPlaceClick: (Place) -> Unit
    ) :
        RecyclerView.ViewHolder(nearbyPlaceItemBinding.root) {

        fun load(place: Place) = with(itemView) {
            setOnClickListener { onPlaceClick(placesList[adapterPosition]) }

            nearbyPlaceItemBinding.imvNearbyPlaceIcon.loadImage(Uri.parse(place.icon))
            nearbyPlaceItemBinding.txvNearbyPlaceName.text = place.name

            place.rating?.let {
                nearbyPlaceItemBinding.txvNearbyPlaceRating.text = context.getString(
                    R.string.nearby_places_list_item_rating,
                    place.getRatingFormatted()
                )
            } ?: run {
                nearbyPlaceItemBinding.txvNearbyPlaceRating.visibility = View.GONE
            }

            place.openNow?.let {
                nearbyPlaceItemBinding.txvNearbyPlaceOpenNow.text = if (it) {
                    context.getString(R.string.nearby_places_list_item_open)
                } else {
                    context.getString(R.string.nearby_places_list_item_closed)
                }
            } ?: run {
                nearbyPlaceItemBinding.txvNearbyPlaceOpenNow.visibility = View.GONE
            }
        }
    }
}