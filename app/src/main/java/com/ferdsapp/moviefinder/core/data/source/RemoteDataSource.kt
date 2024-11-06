package com.ferdsapp.moviefinder.core.data.source

import android.util.Log
import com.ferdsapp.moviefinder.BuildConfig
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.source.network.ApiService
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource private constructor(private val apiService: ApiService) {

    //init manual injection
    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource {
            return instance ?: synchronized(this){
                instance ?: RemoteDataSource(service)
            }
        }
    }

    //function here

    suspend fun getMovieNowPlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>{
        return flow {
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.getMoviePlayingList(
                    authToken = "Bearer $token",
                    query = mapOf(
                        "language" to "en-US",
                        "page" to "1"
                    )
                )
                val dataArray = response.results
                if (dataArray.isNotEmpty()){
                    Log.d("MovieFinder DataSource", "response not empty")
                    emit(ApiResponse.Success(dataArray))
                }else{
                    Log.d("MovieFinder DataSource", "response empty")
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                Log.d("MovieFinder DataSource", "response Error")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTvShowNowPlaying(): Flow<ApiResponse<ArrayList<ItemTvShowPlaying>>>{
        return flow {
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.getTvShowPlayingList(
                    authToken = "Bearer $token",
                    query = mapOf(
                        "language" to "en-US",
                        "page" to "1"
                    )
                )
                val itemData = response.results
                if (itemData.isNotEmpty()){
                    Log.d("MovieFinder DataSource", "response tvShow not empty")
                    emit(ApiResponse.Success(itemData))
                }else{
                    Log.d("MovieFinder DataSource", "response tvShow empty")
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                Log.d("MovieFinder DataSource", "response tvShow error")
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}