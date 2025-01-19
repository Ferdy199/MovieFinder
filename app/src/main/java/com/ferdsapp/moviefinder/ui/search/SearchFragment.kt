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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.databinding.FragmentSearchBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.viewModel.search.SearchViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchAdapter: MovieAdapter by lazy { MovieAdapter() }
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
        if (activity != null){
          binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
              override fun onQueryTextSubmit(query: String?): Boolean {
                  if (query != null) {
                      searchViewModel.getSearch(query).observe(viewLifecycleOwner){ searchResponse ->
                          when(searchResponse){
                              is Resource.Empty -> {
                                  Log.d("SearchFragment", "Empty")
                                  binding.rvSearchLoading.visibility = View.GONE
                              }

                              is Resource.Error -> {
                                  Log.d("SearchFragment", "Error: ${searchResponse.message}")
                                  binding.rvSearchLoading.visibility = View.GONE
                              }

                              is Resource.Loading -> {
                                  binding.rvSearchLoading.visibility = View.VISIBLE
                              }

                              is Resource.Success -> {
                                  searchAdapter.setMovie(searchResponse.data)
                                  binding.rvSearchLoading.visibility = View.GONE
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
}