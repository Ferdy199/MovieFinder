package com.ferdsapp.moviefinder.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.databinding.ActivityMainBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MovieAdapter()
        val factory = ViewModelFactory.getInstance(this)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.movie.observe(this){ movieItem ->
            if (movieItem != null){
                when(movieItem){
                    is ApiResponse.Success -> {
                        Log.d("MovieFinder Activity", "response Success")
                        adapter.setMovie(movieItem.data)
                    }
                    is ApiResponse.Empty -> {

                    }
                    is ApiResponse.Error -> {

                    }
                }
            }
        }

        with(binding.rvMovie){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }
}