package com.example.tawkto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tawkto.R

class StateAdapter : LoadStateAdapter<StateAdapter.TheLoadViewHolder>() {


    class TheLoadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): TheLoadViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.load_footer, parent, false)
                return TheLoadViewHolder(view)
            }
        }

        val pBar: ProgressBar = view.findViewById(R.id.loadBar)
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                pBar.visibility = View.VISIBLE
            } else {
                pBar.visibility = View.GONE
            }
        }

    }

    override fun onBindViewHolder(holder: TheLoadViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TheLoadViewHolder {
       return TheLoadViewHolder.getInstance(parent)
    }
}