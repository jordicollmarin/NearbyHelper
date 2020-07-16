package cat.jorcollmar.nearbyhelper.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(
    liveData: L,
    body: (T) -> Unit,
    onNull: () -> Unit
) {
    liveData.observe(this, Observer { result ->
        result?.let(body) ?: onNull()
    })
}
