package com.ferdsapp.moviefinder.ui.utils

import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter

interface OnItemClickListener {
    fun onItemClick(position: Int, adapter: MovieAdapter)
}