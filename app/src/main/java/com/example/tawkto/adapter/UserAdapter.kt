package com.example.tawkto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tawkto.databinding.RowUserItemBinding
import com.example.tawkto.model.User
import com.example.tawkto.ui.userlistscreen.UserListFragmentDirections

class UserAdapter : ListAdapter<User,
        UserAdapter.TheViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TheViewHolder {
        return TheViewHolder(
            RowUserItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: TheViewHolder, position: Int) {
        val marsPhoto = getItem(position)
        holder.bind(marsPhoto)
    }


    class TheViewHolder(
        private var binding:
        RowUserItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
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