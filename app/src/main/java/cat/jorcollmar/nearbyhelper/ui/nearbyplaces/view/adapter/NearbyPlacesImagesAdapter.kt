package cat.jorcollmar.nearbyhelper.ui.nearbyplaces.view.adapter

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import cat.jorcollmar.nearbyhelper.commons.extensions.loadImageCenterCrop

class NearbyPlacesImagesAdapter : PagerAdapter() {
    private var imagesList: MutableList<String> = mutableListOf()

    fun updateItems(pages: List<String>) {
        imagesList = pages.toMutableList()
        notifyDataSetChanged()
    }

    fun setEmptyState() {
        imagesList.clear()
        imagesList.add("")
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.loadImageCenterCrop(Uri.parse(imagesList[position]))
        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val imageView = `object` as View
        viewPager.removeView(imageView)
    }

    override fun getCount(): Int {
        return imagesList.size
    }
}
