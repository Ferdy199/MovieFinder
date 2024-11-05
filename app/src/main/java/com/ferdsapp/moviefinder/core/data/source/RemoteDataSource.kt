package com.ferdsapp.moviefinder.core.data.source

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
                        "language" to "en",
                        "page" to "1"
                    )
                )
                val dataArray = response.awaitResponse().body()?.results
                if (dataArray!!.isNotEmpty()){
                    emit(ApiResponse.Success(dataArray))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}