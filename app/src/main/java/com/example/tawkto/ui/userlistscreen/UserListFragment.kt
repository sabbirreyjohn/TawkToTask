package com.example.tawkto.ui.userlistscreen


import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.tawkto.R
import com.example.tawkto.adapter.PagingAdapter
import com.example.tawkto.adapter.StateAdapter
import com.example.tawkto.databinding.FragmentUserListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    lateinit var binding: FragmentUserListBinding
    lateinit var adapter: PagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = PagingAdapter().also {
            binding.rcvPictures.adapter = it.withLoadStateFooter(StateAdapter())
            binding.rcvPictures.adapter!!.notifyDataSetChanged()
        }
        binding.rcvPictures.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadUsersFromDB().collectLatest {
                adapter.submitData(it)
            }


        }
       setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.search_bar)

        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.loadUsersFromDB().collectLatest {
                        adapter.submitData(it.filter {
                            it.userName.contains(query!!.toRegex())
                        })
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isNullOrBlank()){
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.loadUsersFromDB().collectLatest {
                            adapter.submitData(it)
                        }
                    }
                }else{
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.loadUsersFromDB().collectLatest {
                            adapter.submitData(it.filter {
                                it.userName.contains(newText!!.toRegex())
                            })
                        }
                    }
                }
                return true
            }
        })
    }
}