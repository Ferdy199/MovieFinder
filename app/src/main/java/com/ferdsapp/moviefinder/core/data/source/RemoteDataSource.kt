package com.ferdsapp.moviefinder.core.data.source

import android.util.Log
import com.ferdsapp.moviefinder.BuildConfig
import com.ferdsapp.moviefinder.core.data.model.network.detail.DetailResponses
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.model.network.search.ListSearchResponse
import com.ferdsapp.moviefinder.core.data.model.network.search.SearchResponse
import com.ferdsapp.moviefinder.core.data.source.network.ApiService
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) {

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
                emit(ApiResponse.Loading)
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

    suspend fun getSearch(name:String) : Flow<ApiResponse<List<ListSearchResponse>>> {
        return flow {
            emit(ApiResponse.Loading)
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.getSearch(
                    authToken = "Bearer $token",
                    query = mapOf(
                        "query" to name
                    )
                )
                if (response.results.isNotEmpty()){
                    val filterList = response.results.filter {
                        it.media_type != "person"
                    }
                    emit(ApiResponse.Success(filterList))
                }else{
                    emit(ApiResponse.Empty)
                }

            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString(), null))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetail(type: String, id: String) : Flow<ApiResponse<DetailResponses>>{
        return flow {
            emit(ApiResponse.Loading)
            try {
                val token = BuildConfig.API_TOKEN
                val response = apiService.getDetails(
                    authToken = "Bearer $token",
                    type = type,
                    id = id
                )
                if (response != null){
                    Log.d("remoteSource", "getDetail: success")
                    emit(ApiResponse.Success(response))
                }else{
                    Log.d("remoteSource", "getDetail: Empty")
                    emit(ApiResponse.Empty)
                }

            }catch (e: Exception){
                Log.d("remoteSource", "getDetail: Error")
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    private inline fun <reified T> gsonToResponse(json: String) : T? {
        return try {
            gson.fromJson(json, T::class.java)
        }catch (e: Exception){
            null
        }
    }
}