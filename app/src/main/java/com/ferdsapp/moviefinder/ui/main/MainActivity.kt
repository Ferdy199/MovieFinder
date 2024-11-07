package com.ferdsapp.moviefinder.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.databinding.ActivityMainBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val movieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val tvShowAdapter: MovieAdapter by lazy { MovieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        liveData: LiveData<ApiResponse<ArrayList<T>>>,
        onSuccess: (List<T>) -> Unit
    ){
        liveData.observe(this){ apiResponse ->
            when(apiResponse){
                is ApiResponse.Success -> {
                    Log.d("MainActivity", "response Success")
                    onSuccess(apiResponse.data)
                }
                is ApiResponse.Empty -> {
                    Log.d("MainActivity", "response Empty")
                }
                is ApiResponse.Error -> {
                    Log.d("MainActivity", "response Error: ${apiResponse.errorMessage}")
                }
            }
        }
    }
}