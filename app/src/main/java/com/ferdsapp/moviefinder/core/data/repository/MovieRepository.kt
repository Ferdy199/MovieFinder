package com.ferdsapp.moviefinder.core.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.ferdsapp.moviefinder.core.data.model.entity.login.LoginEntity
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import com.ferdsapp.moviefinder.core.utils.Constant
import com.ferdsapp.moviefinder.core.utils.DataMapper
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
    override fun getMoviePlaying(): Flow<Resource<ArrayList<MovieEntity>>> {
        return flow<Resource<ArrayList<MovieEntity>>> {
            try {
                remoteDataSource.getMovieNowPlaying().collect{ apiResponse ->
                    when(apiResponse){
                        is ApiResponse.Success -> {
                            val movieList = DataMapper.mapResponsestMovieEntities(apiResponse.data)
                            emit(Resource.Success(movieList))
                        }
                        is ApiResponse.Empty -> {
                            emit(Resource.Empty)
                        }
                        is ApiResponse.Error -> {
                            emit(Resource.Error(apiResponse.errorMessage))
                        }

                        is ApiResponse.Loading -> {
                            emit(Resource.Loading)
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTvShowPlaying(): Flow<Resource<ArrayList<TvShowEntity>>> {

        return flow<Resource<ArrayList<TvShowEntity>>> {
            try {
                remoteDataSource.getTvShowNowPlaying().collect{ apiResponse ->
                    when(apiResponse){
                        is ApiResponse.Success -> {
                            val tvShowList = DataMapper.mapResponsesTvShowEntities(apiResponse.data)
                            emit(Resource.Success(tvShowList))
                        }
                        is ApiResponse.Empty -> {
                            emit(Resource.Empty)
                        }
                        is ApiResponse.Error -> {
                            emit(Resource.Error(apiResponse.errorMessage))
                        }

                        is ApiResponse.Loading -> {
                            emit(Resource.Loading)
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getTokenLogin(): Flow<Resource<GetTokenLogin>> {
        return flow<Resource<GetTokenLogin>> {
            try {
                remoteDataSource.getLoginToken().collect { apiResponse ->
                    when(apiResponse){
                        is ApiResponse.Success -> {
                            if (apiResponse.data.success){
                                val dataToken = apiResponse.data
                                saveRequestToken(dataToken.request_token)
                                emit(Resource.Success(dataToken))
                            }else{
                                emit(Resource.Error(apiResponse.data.toString()))
                            }
                        }
                        is ApiResponse.Empty -> {
                            emit(Resource.Empty)
                        }
                        is ApiResponse.Error -> {
                            emit(Resource.Error(apiResponse.errorMessage, apiResponse.data))
                        }

                        is ApiResponse.Loading -> {
                            emit(Resource.Loading)
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun saveTokenValidate(token: String) {
        Log.d("MovieFinder Repository", "save Token validate")
        sharedPreferences.edit().putString(Constant.SESSION_REQUEST_TOKEN_VALIDATE, token).apply()
    }

    override fun getRequestTokenValidate(): Flow<Resource<String>> {
        return flow {
            try {
                val getValidateToken = sharedPreferences.getString(Constant.SESSION_REQUEST_TOKEN_VALIDATE, "")
                if (!getValidateToken.isNullOrEmpty()){
                    emit(Resource.Success(getValidateToken))
                }else{
                    emit(Resource.Empty)
                }
            }catch (e:Exception){
                Log.d("MovieFinder Repository", "getRequestTokenValidate: ${e.message}")
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
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
        }.flowOn(Dispatchers.IO)
    }

    override fun isSessionValid(session: String): Flow<Boolean>  {
        return flow {
            // Tanggal target sesi dalam format UTC

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parsing tanggal target
            val targetDate = dateFormat.parse(session)

            // Mendapatkan waktu saat ini
            val currentDate = Date()

            Log.d("MovieFinder Repository", "isSessionValid date: $currentDate || $targetDate || ${currentDate.before(targetDate)}")
            // Membandingkan apakah waktu saat ini sebelum waktu target
            emit(currentDate.before(targetDate))
        }.flowOn(Dispatchers.IO)
    }

    override fun loginProcess(username: String, password: String): Flow<Resource<LoginEntity>> {
        return flow {
            try {
                val requestToken = sharedPreferences.getString(Constant.REQUEST_TOKEN, "")
                Log.d("MovieFinder Repository", "loginProcess token: $requestToken")
                if (!requestToken.isNullOrEmpty()){
                    remoteDataSource
                        .loginProcess(requestToken, username, password)
                        .collect { apiResponse ->
                            when(apiResponse){
                                is ApiResponse.Success -> {
                                    val loginEntity = DataMapper.mapLoginResponsesEntities(apiResponse.data)
                                    emit(Resource.Success(loginEntity))
                                }
                                is ApiResponse.Empty -> {
                                    emit(Resource.Empty)
                                }
                                is ApiResponse.Error -> {
                                    val loginEntity = apiResponse.data?.let { DataMapper.mapLoginResponsesEntities(it) }
                                    Log.d("MovieFinder Repository", "loginProcess error: $loginEntity")
                                    emit(Resource.Error(apiResponse.errorMessage, data = loginEntity))
                                }

                                is ApiResponse.Loading -> {
                                    emit(Resource.Loading)
                                }
                            }
                        }
                }else{
                    emit(Resource.Empty)
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}