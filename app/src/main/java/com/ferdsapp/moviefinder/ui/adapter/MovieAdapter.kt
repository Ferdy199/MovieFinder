package com.ferdsapp.moviefinder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.databinding.ItemsListHorizontalBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listMovie = ArrayList<ItemMovePlaying>()

    fun setMovie(movie: List<ItemMovePlaying>){
        this.listMovie.clear()
        this.listMovie.addAll(movie)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsListHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemsListHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: ItemMovePlaying){
            with(binding){
                tvTitle.text = movie.original_title
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                    .into(imgPoster)
            }
        }
    }
}