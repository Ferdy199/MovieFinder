package com.ferdsapp.moviefinder.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.databinding.FragmentHomeBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val movieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val tvShowAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val homeViewModel: MainViewModel by viewModels{
       factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null){
            observeApiResponse(homeViewModel.movie, movieAdapter::setMovie)
            observeApiResponse(homeViewModel.tvShow, tvShowAdapter::setMovie)

            recyclerViewConfig(binding.rvMovie, movieAdapter)
            recyclerViewConfig(binding.rvTvShow, tvShowAdapter)

        }
    }

    private fun recyclerViewConfig(recyclerView: RecyclerView, adapter: MovieAdapter){
        with(recyclerView){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }

    private fun <T> observeApiResponse(
        liveData: LiveData<Resource<ArrayList<T>>>,
        onSuccess: (List<T>) -> Unit
    ){
        liveData.observe(viewLifecycleOwner){ apiResponse ->
            when(apiResponse){
                is Resource.Success -> {
                    Log.d("MainActivity", "response Success ${apiResponse.data}")
                    onSuccess(apiResponse.data)
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE
                }
                is Resource.Empty -> {
                    Log.d("MainActivity", "response Empty")
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE

                }
                is Resource.Error -> {
                    Log.d("MainActivity", "response Error: ${apiResponse.message}")
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.loadingShimmer1.visibility = View.VISIBLE
                    binding.loadingShimmer2.visibility = View.VISIBLE
                    Log.d("MainActivity", "response Loading")
                }
            }
        }
    }
}