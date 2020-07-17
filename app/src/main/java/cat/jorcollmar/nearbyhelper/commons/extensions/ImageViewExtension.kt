package cat.jorcollmar.nearbyhelper.commons.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import cat.jorcollmar.nearbyhelper.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.loadImage(uri: Uri?) {
    Picasso
        .get()
        .load(uri)
        .placeholder(R.drawable.ic_placeholder)
        .into(this, object : Callback {
            override fun onSuccess() {}

            override fun onError(e: Exception) {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image_error))
            }
        })
}

fun ImageView.loadImageFit(uri: Uri?) {
    Picasso
        .get()
        .load(uri)
        .placeholder(R.drawable.ic_placeholder)
        .into(this, object : Callback {
            override fun onSuccess() {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            override fun onError(e: Exception) {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image_error))
            }
        })
}