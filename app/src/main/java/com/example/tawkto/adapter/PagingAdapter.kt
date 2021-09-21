package com.example.tawkto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tawkto.R
import com.example.tawkto.databinding.RowUserItemBinding
import com.example.tawkto.databinding.RowUserItemPagerBinding
import com.example.tawkto.model.User
import com.example.tawkto.ui.userlistscreen.UserListFragmentDirections

class PagingAdapter : PagingDataAdapter<User,
        PagingAdapter.TheViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TheViewHolder {
        return TheViewHolder(
            RowUserItemPagerBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: TheViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            holder.bind(user)
        }
    }


    class TheViewHolder(
        private var binding:
        RowUserItemPagerBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            binding.tvName.text = user.userName
            binding.tvNode.text = user.nodeId
            user.note?.let {
                binding.ivNote.visibility = View.VISIBLE
            }
            user.userAvatar?.let {
                val imageUri = it.toUri().buildUpon().scheme("https").build()
                binding.ivAvatar.load(imageUri) {
                    placeholder(R.drawable.ic_baseline_cached_24)
                    error(R.drawable.ic_baseline_error_24)
                }
            }
            binding.root.setOnClickListener {
                val action = UserListFragmentDirections.actionUserListFragmentToProfileFragment(user)
                binding.root.findNavController().navigate(action)
            }

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userAvatar == newItem.userAvatar
        }
    }
}