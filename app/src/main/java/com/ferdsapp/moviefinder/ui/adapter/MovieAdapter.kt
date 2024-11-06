package com.ferdsapp.moviefinder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.databinding.ItemsListHorizontalBinding

class MovieAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem = ArrayList<Any>()

    fun setMovie(movie: List<Any>){
        this.listItem.clear()
        this.listItem.addAll(movie)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is ItemMovePlaying -> ITEM_MOVIE
            is ItemTvShowPlaying -> ITEM_TV_SHOW
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            ITEM_MOVIE -> {
                val binding = ItemsListHorizontalBinding.inflate(inflater, parent, false)
                MovieViewHolder(binding)
            }
            ITEM_TV_SHOW -> {
                val binding = ItemsListHorizontalBinding.inflate(inflater, parent, false)
                TvShowViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MovieViewHolder -> holder.bind(listItem[position] as ItemMovePlaying)
            is TvShowViewHolder -> holder.bind(listItem[position] as ItemTvShowPlaying)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
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

    inner class TvShowViewHolder(private val binding: ItemsListHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: ItemTvShowPlaying) {
            with(binding) {
                tvTitle.text = tvShow.original_name
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + tvShow.poster_path)
                    .into(imgPoster)
            }
        }
    }

    companion object {
        private const val ITEM_MOVIE = 1
        private const val ITEM_TV_SHOW = 2
    }
}