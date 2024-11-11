package com.ferdsapp.moviefinder.core.domain.repository

import android.content.SharedPreferences
import android.util.Log
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MovieRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferences
) : IMoveRepository {
    //init manual injection
    companion object{
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            sharedPreferences: SharedPreferences
        ) : MovieRepository {
            return instance ?: synchronized(this){
                instance ?: MovieRepository(remoteDataSource, sharedPreferences)
            }
        }
    }

    //function here
    override fun getMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>> {
        return flow {
            try {
                remoteDataSource
                    .getMovieNowPlaying()
                    .collect { apiResponse ->
                        when(apiResponse){
                            is ApiResponse.Success -> {
                                Log.d("MovieFinder Repository", "response Success")
                                emit(ApiResponse.Success(apiResponse.data))
                            }
                            is ApiResponse.Empty -> {
                                Log.d("MovieFinder Repository", "response Empty")
                                emit(ApiResponse.Empty)
                            }
                            is ApiResponse.Error -> {
                                Log.d("MovieFinder Repository", "response Error")
                                emit(ApiResponse.Error(apiResponse.errorMessage))
                            }
                        }
                    }
            }catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTvShowPlaying(): Flow<ApiResponse<ArrayList<ItemTvShowPlaying>>> {
        return flow {
            try {
                remoteDataSource
                    .getTvShowNowPlaying()
                    .collect { apiResponse ->
                        when(apiResponse){
                            is ApiResponse.Success -> {
                                Log.d("MovieFinder Repository", "response tvShow Success")
                                emit(ApiResponse.Success(apiResponse.data))
                            }
                            is ApiResponse.Empty -> {
                                Log.d("MovieFinder Repository", "response tvShow Empty")
                                emit(ApiResponse.Empty)
                            }
                            is ApiResponse.Error -> {
                                Log.d("MovieFinder Repository", "response tvShow Error")
                                emit(ApiResponse.Error(apiResponse.errorMessage))
                            }
                        }
                    }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTokenLogin(): Flow<ApiResponse<GetTokenLogin>> {
        return flow {
            try {
                remoteDataSource.getLoginToken().collect { apiResponse ->
                    when(apiResponse){
                        is ApiResponse.Success -> {
                            if (apiResponse.data.success){
                                val dataToken = apiResponse.data
                                saveRequestToken(dataToken.request_token)
                                emit(ApiResponse.Success(dataToken))
                            }else{
                                emit(ApiResponse.Error(apiResponse.data.toString()))
                            }
                        }
                        is ApiResponse.Empty -> {
                            emit(ApiResponse.Empty)
                        }
                        is ApiResponse.Error -> {
                            emit(ApiResponse.Error(apiResponse.errorMessage))
                        }
                    }
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.message.toString()))
            }
        }
    }

    override fun saveTokenValidate(token: String) {
        Log.d("MovieFinder Repository", "save Token validate")
        sharedPreferences.edit().putString(Constant.REQUEST_TOKEN_VALIDATE, token).apply()
    }

    override fun getRequestTokenValidate(): Flow<String> {
        return flow {
            try {
                val getValidateToken = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "")
                if (!getValidateToken.isNullOrEmpty()){
                    emit(getValidateToken)
                }else{
                    Log.d("MovieFinder Repository", "failed getRequestTokenValidate")
                }
            }catch (e:Exception){
                Log.d("MovieFinder Repository", "getRequestTokenValidate: ${e.message}")
            }
        }
    }


    override fun saveRequestToken(token: String){
        Log.d("MovieFinder Repository", "save request token")
        sharedPreferences.edit().putString(Constant.REQUEST_TOKEN, token).apply()
    }

    override fun getRequestToken(): Flow<String> {
        return flow {
            val requestToken = sharedPreferences.getString(Constant.REQUEST_TOKEN, "")
            if (!requestToken.isNullOrEmpty()){
                Log.d("MovieFinder Repository", "getRequestToken: sendToken $requestToken")
                emit(requestToken)
            }else{
                Log.d("MovieFinder Repository", "failed getRequestToken")
            }
        }
    }

    override fun isSessionValid(session: String): Flow<Boolean>  {
        return flow {
            // Tanggal target sesi dalam format UTC
            val targetDateString = session
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parsing tanggal target
            val targetDate = dateFormat.parse(targetDateString)

            // Mendapatkan waktu saat ini
            val currentDate = Date()

            // Membandingkan apakah waktu saat ini sebelum waktu target
            emit(currentDate.before(targetDate))
        }
    }


}