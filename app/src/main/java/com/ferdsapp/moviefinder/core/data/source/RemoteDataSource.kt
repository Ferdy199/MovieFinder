package com.ferdsapp.moviefinder.core.data.source

import android.util.Log
import com.ferdsapp.moviefinder.BuildConfig
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.data.source.network.ApiService
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ListMoviePlaying
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.awaitResponse

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
}