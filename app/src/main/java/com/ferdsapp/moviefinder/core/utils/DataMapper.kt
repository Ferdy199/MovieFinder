package com.ferdsapp.moviefinder.core.utils

import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.core.data.model.entity.login.LoginEntity
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.search.ListSearchEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.detail.DetailResponses
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.model.network.search.ListSearchResponse
import com.ferdsapp.moviefinder.core.data.model.network.search.SearchResponse

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
                media_type = it.media_type,
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
                media_type = it.media_type,
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

    fun mapSearchResponsesEntities(input: ArrayList<ListSearchResponse>): ArrayList<ListSearchEntity> {
        val searchList = ArrayList<ListSearchEntity>()

        input.map {
            val search = ListSearchEntity(
                adult = it.adult,

                backdrop_path = it.backdrop_path,

                id = it.id,

                title = it.title,

                original_language = it.original_language,

                original_title = it.original_title,

                overview = it.overview,

                poster_path = it.poster_path,

                media_type = it.media_type,

                genre_ids = it.genre_ids ,

                popularity = it.popularity,

                release_date = it.release_date,

                vote_average = it.vote_average,

                vote_count = it.vote_count,
            )
            searchList.add(search)
        }
        return searchList
    }

    fun mapDetailMovieEntities(input: DetailResponses) : DetailEntity {
        return DetailEntity(
            adult = input.adult,
            backdrop_path = input.backdrop_path,
            budget = input.budget,
            genres = input.genres,
            homepage = input.homepage,
            id = input.id,
            original_language = input.original_language,
            original_title = input.original_title,
            original_name = input.original_name,
            overview = input.overview,
            release_date = input.release_date,
            first_air_date = input.first_air_date,
            poster_path = input.poster_path,
            title = input.title,
            name = input.name,
            vote_average = input.vote_average,
            popularity = input.popularity,
            vote_count = input.vote_count,
            video = input.video,
            status = input.status,
            tagline = input.tagline
        )
    }

}