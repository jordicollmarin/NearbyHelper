package cat.jorcollmar.nearbyhelper.common.extension

import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.loadImage(uri: Uri?) {
    Picasso
        .get()
        .load(uri)
        .into(this, object : Callback {
            override fun onSuccess() {}
            override fun onError(e: Exception) {}
        })
}