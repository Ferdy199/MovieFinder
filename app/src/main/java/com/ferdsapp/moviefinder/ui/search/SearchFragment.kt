package com.ferdsapp.moviefinder.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.core.data.model.entity.search.ListSearchEntity
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.databinding.FragmentSearchBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.ui.detail.DetailFragment
import com.ferdsapp.moviefinder.ui.main.MainActivity
import com.ferdsapp.moviefinder.ui.utils.Constant
import com.ferdsapp.moviefinder.ui.utils.OnItemClickListener
import com.ferdsapp.moviefinder.viewModel.search.SearchViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment(), OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var detailFragment: DetailFragment

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchAdapter: MovieAdapter by lazy { MovieAdapter(this) }
    private val searchViewModel: SearchViewModel by viewModels {
        factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEmptyView(true)
        if (activity != null){
            setFragmentResultListener("emptyView"){_, bundle ->
                val isBack = bundle.getBoolean("isBack", false)
                if (isBack){
                    showEmptyView(false)
                }else{
                    showEmptyView(true)
                }
            }

            binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
              override fun onQueryTextSubmit(query: String?): Boolean {
                  if (query != null) {
                      searchViewModel.getSearch(query).observe(viewLifecycleOwner){ searchResponse ->
                          when(searchResponse){
                              is Resource.Empty -> {
                                  Log.d("SearchFragment", "Empty")
                                  binding.rvSearchLoading.visibility = View.GONE
                                  showEmptyView(true)
                              }

                              is Resource.Error -> {
                                  Log.d("SearchFragment", "Error: ${searchResponse.message}")
                                  binding.rvSearchLoading.visibility = View.GONE
                                  showEmptyView(false)
                              }

                              is Resource.Loading -> {
                                  binding.rvSearchLoading.visibility = View.VISIBLE
                                  binding.rvSearch.visibility = View.GONE
                                  showEmptyView(false)
                              }

                              is Resource.Success -> {
                                  searchAdapter.submitList(searchResponse.data as List<Any>)
                                  binding.rvSearchLoading.visibility = View.GONE
                                  binding.rvSearch.visibility = View.VISIBLE
                                  showEmptyView(false)
                              }
                          }

                      }
                  }
                  binding.searchBar.clearFocus()
                  return true
              }

              override fun onQueryTextChange(newText: String?): Boolean {
                  return false
              }

          })
        }
        with(binding.rvSearch){
            layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            this.adapter = searchAdapter
        }
    }

    override fun onItemClick(position: Int, adapter: MovieAdapter) {
        when(val item = adapter.currentList[position]){
            is ListSearchEntity -> {
                showEmptyView(false)
                observeDetail(item.media_type.toString(), item.id!!)
            }
        }

    }

    private fun observeDetail(mediaType: String, id: Int){
        searchViewModel.getDetailMovie(mediaType, id).observe(viewLifecycleOwner){ detailData ->
            when(detailData){
                is Resource.Empty -> {
                    showLoading(false)
                }
                is Resource.Error -> {
                    Log.d("Home Fragment", "onItemClick: Error ${detailData.message}")
                    showLoading(false)
                }
                is Resource.Loading -> {
                    showLoading(true)

                }
                is Resource.Success -> {
                    Log.d("Home Fragment", "onItemClick: Success")
                    showLoading(false)
                    showEmptyView(false)
                    val bundle = Bundle().apply {
                        putParcelable(Constant.DATA_ITEM, detailData.data)
                    }
                    detailFragment.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        (requireActivity() as MainActivity).showLoading(isLoading)
    }

    private fun showEmptyView(isShowing: Boolean){
        when(isShowing){
            true -> {
                binding.emptyView.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.VISIBLE
            }
            false -> {
                binding.emptyView.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
            }
        }
    }
}