package com.ferdsapp.moviefinder.core.data.source

import android.util.Log
import com.ferdsapp.moviefinder.BuildConfig
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.source.network.ApiService
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RemoteDataSource private constructor(
    private val apiService: ApiService,
    private val gson: Gson
) {

    //init manual injection
    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService, gson: Gson): RemoteDataSource {
            return instance ?: synchronized(this){
                instance ?: RemoteDataSource(service, gson)
            }
        }
    }

    //function here

    suspend fun getMovieNowPlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>{
        return flow {
            emit(ApiResponse.Loading)
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
            emit(ApiResponse.Loading)
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

    suspend fun getLoginToken(): Flow<ApiResponse<GetTokenLogin>>{
        return flow {
            emit(ApiResponse.Loading)
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.getLoginToken(
                    authToken = "Bearer $token"
                )
                when(response.success){
                    true -> {
                        emit(ApiResponse.Success(response))
                    }
                    false -> {
                        emit(ApiResponse.Error(response.success.toString(), response))
                    }
                }

            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))

            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loginProcess(requestToken: String, username: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            emit(ApiResponse.Loading)
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.loginProcess(
                    authToken = "Bearer $token",
                    requestToken = requestToken,
                    username = username,
                    password = password
                )
                when(response.success){
                    true -> {
                        emit(ApiResponse.Success(response))
                    }
                    false -> {
                        emit(ApiResponse.Error(response.success.toString(), data = response))
                    }
                }
            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = gsonToResponse<LoginResponse>(errorBody.toString())
                Log.d("MovieFinder DataSource", "loginProcess error: $errorBody")
                emit(ApiResponse.Error(e.message.toString(), data = errorResponse))
            } catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    private inline fun <reified T> gsonToResponse(json: String) : T? {
        return try {
            gson.fromJson(json, T::class.java)
        }catch (e: Exception){
            null
        }
    }
}