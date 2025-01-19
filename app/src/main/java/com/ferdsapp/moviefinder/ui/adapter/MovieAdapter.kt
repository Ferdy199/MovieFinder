package com.ferdsapp.moviefinder.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.search.ListSearchEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.databinding.ItemListSearchBinding
import com.ferdsapp.moviefinder.databinding.ItemsListHorizontalBinding

class MovieAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem = ArrayList<Any>()

    fun setMovie(movie: List<Any>){
        this.listItem.clear()
        this.listItem.addAll(movie)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is MovieEntity -> ITEM_MOVIE
            is TvShowEntity -> ITEM_TV_SHOW
            is ListSearchEntity -> ITEM_SEARCH
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
            ITEM_SEARCH -> {
                val binding = ItemListSearchBinding.inflate(inflater, parent, false)
                SearchViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MovieViewHolder -> holder.bind(listItem[position] as MovieEntity)
            is TvShowViewHolder -> holder.bind(listItem[position] as TvShowEntity)
            is SearchViewHolder -> holder.bind(listItem[position] as ListSearchEntity)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }


    inner class MovieViewHolder(private val binding: ItemsListHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieEntity){
            with(binding){
                tvTitle.text = movie.original_title
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                    .into(imgPoster)
            }
        }
    }

    inner class TvShowViewHolder(private val binding: ItemsListHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShowEntity) {
            with(binding) {
                tvTitle.text = tvShow.original_name
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500" + tvShow.poster_path)
                    .into(imgPoster)
            }
        }
    }

    inner class SearchViewHolder(private val binding: ItemListSearchBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(search: ListSearchEntity){
           with(binding){
               val screenWidth = Resources.getSystem().displayMetrics.widthPixels
               val columnWidth = (screenWidth / 2) - 16 // Margin 4dp di kiri dan kanan (8dp total)
               val imageHeight = (columnWidth * 3) / 2 // Rasio 2:3
               Glide.with(itemView.context)
                   .load("https://image.tmdb.org/t/p/w500" + search.poster_path)
                   .override(columnWidth, imageHeight)
                   .placeholder(R.drawable.logo)
                   .error(R.drawable.ic_broken_image_24)
                   .into(imgPoster)
           }
        }
    }

    companion object {
        private const val ITEM_MOVIE = 1
        private const val ITEM_TV_SHOW = 2
        private const val ITEM_SEARCH = 3
    }
}