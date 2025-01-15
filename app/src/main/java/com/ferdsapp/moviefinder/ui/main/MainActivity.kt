package com.ferdsapp.moviefinder.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.databinding.ActivityMainBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory : ViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    private val movieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val tvShowAdapter: MovieAdapter by lazy { MovieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeApiResponse(mainViewModel.movie, movieAdapter::setMovie)
        observeApiResponse(mainViewModel.tvShow, tvShowAdapter::setMovie)

        recyclerViewConfig(binding.rvMovie, movieAdapter)
        recyclerViewConfig(binding.rvTvShow, tvShowAdapter)

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
        liveData.observe(this){ apiResponse ->
            when(apiResponse){
                is Resource.Success -> {
                    Log.d("MainActivity", "response Success")
                    onSuccess(apiResponse.data)
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE
                }
                is Resource.Empty -> {
                    Log.d("MainActivity", "response Empty")
                }
                is Resource.Error -> {
                    Log.d("MainActivity", "response Error: ${apiResponse.message}")
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