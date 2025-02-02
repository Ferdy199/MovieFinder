package com.ferdsapp.moviefinder.core.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.core.data.model.entity.login.LoginEntity
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.search.ListSearchEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferences
) : IMoveRepository {

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

    override fun getRequestToken(): Flow<Resource<GetTokenLogin>> {
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
                       }                    }
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
                Log.d("Movie Repository", "getRequestTokenValidate: $getValidateToken")
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

    override fun getTokenLogin(): Flow<String> {
        return flow {
            val sessionLoginToken = sharedPreferences.getString(Constant.SESSION_LOGIN_TOKEN_VALIDATE, "")
            if (!sessionLoginToken.isNullOrEmpty()){
                Log.d("MovieFinder Repository", "getTokenLogin: getTokenLogin $sessionLoginToken")
                emit(sessionLoginToken)
            }else{
                emit("failed")
                Log.d("MovieFinder Repository", "failed getTokenLogin")
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
                                    sharedPreferences.edit().putString(Constant.LOGIN_TOKEN, loginEntity.request_token).apply()
                                    sharedPreferences.edit().putString(Constant.SESSION_LOGIN_TOKEN_VALIDATE, loginEntity.expires_at).apply()
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

    override fun getSearch(search: String): Flow<Resource<ArrayList<ListSearchEntity>>> {
        return flow {
            try {
                remoteDataSource.getSearch(search).collect { searchResponse ->
                    when(searchResponse){
                        is ApiResponse.Empty -> emit(Resource.Empty)
                        is ApiResponse.Error -> emit(Resource.Error(searchResponse.errorMessage, null))
                        is ApiResponse.Loading -> emit(Resource.Loading)
                        is ApiResponse.Success -> {
                            if(searchResponse.data != null){
                                val searchList = DataMapper.mapSearchResponsesEntities(searchResponse.data)
                                emit(Resource.Success(searchList))
                            }else{
                                emit(Resource.Empty)
                            }
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString(), null))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getDetailMovie(type: String, id: Int): Flow<Resource<DetailEntity>> {
        return flow {
            try {
                remoteDataSource.getDetail(type, id.toString()).collect { detailResponses ->
                    when(detailResponses){
                        is ApiResponse.Empty -> emit(Resource.Empty)
                        is ApiResponse.Error -> emit(Resource.Error(detailResponses.errorMessage))
                        is ApiResponse.Loading -> emit(Resource.Loading)
                        is ApiResponse.Success -> {
                            val detailEntity = DataMapper.mapDetailMovieEntities(detailResponses.data)
                            emit(Resource.Success(detailEntity))
                        }
                    }
                }
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}