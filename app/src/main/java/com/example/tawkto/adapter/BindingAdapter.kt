package com.example.tawkto.adapter

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tawkto.R
import com.example.tawkto.model.User
import com.example.tawkto.ui.userlistscreen.ApiStatus

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imageUrl: String?) {

    imageUrl?.let {
        val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imageUri) {
            placeholder(R.drawable.ic_baseline_cached_24)
            error(R.drawable.ic_baseline_error_24)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<User>?
) {

    val adapter = recyclerView.adapter as UserAdapter
    adapter.submitList(data)

}

@BindingAdapter("srcByApiStatus")
fun bindByApiStatus(imageView: ImageView, status: ApiStatus) {

    when (status) {

        ApiStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.loading_animation)
        }

        ApiStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }

        ApiStatus.DONE -> {
            imageView.visibility = View.GONE

        }
    }

}