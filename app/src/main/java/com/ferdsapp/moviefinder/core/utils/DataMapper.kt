package com.ferdsapp.moviefinder.core.utils

import com.ferdsapp.moviefinder.core.data.model.entity.login.LoginEntity
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying

object DataMapper {
    fun mapResponsestMovieEntities(input: ArrayList<ItemMovePlaying>): ArrayList<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()
        input.map {
            val movie = MovieEntity(
                adult = it.adult,
                backdrop_path = it.backdrop_path,
                genre_ids = it.genre_ids,
                id = it.id,
                original_language = it.original_language,
                original_title = it.original_title,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                release_date = it.release_date,
                title = it.title,
                vote_average = it.vote_average,
            )
            movieList.add(movie)
        }
       return movieList
    }

    fun mapResponsesTvShowEntities(input: ArrayList<ItemTvShowPlaying>): ArrayList<TvShowEntity> {
        val tvShowList = ArrayList<TvShowEntity>()
        input.map {
            val tvShow = TvShowEntity(
                backdrop_path = it.backdrop_path,
                genre_ids = it.genre_ids,
                id = it.id,
                origin_country = it.origin_country,
                original_language = it.original_language,
                original_name = it.original_name,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                first_air_date = it.first_air_date,
                name = it.name,
                vote_average = it.vote_average,
                vote_count = it.vote_count

            )
            tvShowList.add(tvShow)
        }
        return tvShowList
    }

    fun mapLoginResponsesEntities(input: LoginResponse) = LoginEntity(
        success = input.success,
        status_message = input.status_message,
        request_token = input.request_token,
        status_code = input.status_code,
        expires_at = input.expires_at
    )
}