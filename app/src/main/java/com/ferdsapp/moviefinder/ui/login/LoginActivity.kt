package com.ferdsapp.moviefinder.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.utils.Constant
import com.ferdsapp.moviefinder.databinding.ActivityLoginBinding
import com.ferdsapp.moviefinder.viewModel.login.LoginViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var sharedPreferences: SharedPreferences
    private var tokenExp: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)
        var tokenLogin = ""

        val reqToken = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()

        if (reqToken.isNullOrEmpty()){
            loginViewModel.getTokenLogin.observe(this) { apiResponse ->
                when(apiResponse){
                    is ApiResponse.Success -> {
                        tokenExp = apiResponse.data.expires_at
                        tokenLogin = apiResponse.data.request_token.ifEmpty {
                            sharedPreferences.getString(Constant.REQUEST_TOKEN, "").toString()
                        }
                        Log.d("Login Activity", "tokenLogin : $tokenLogin")
                    }
                    is ApiResponse.Empty -> {
                        Log.d("Login Activity", "response eror : empty")
                    }
                    is ApiResponse.Error -> {
                        Log.d("Login Activity", "response eror : ${apiResponse.errorMessage}")
                    }

                }
            }
        }


        binding.loginButton.setOnClickListener {
            val tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()

            if (tokenValidate.isNullOrEmpty()){
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.themoviedb.org/authenticate/$tokenLogin"))
                startActivity(intent)
            }else{
                val sessionValid = isSessionValid(tokenValidate)
                when(sessionValid){
                    true -> {
                        Toast.makeText(this, "Session Valid", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        if (tokenLogin.isEmpty()){
                            Toast.makeText(this, "Token Kosong", Toast.LENGTH_SHORT).show()
                        }else{
                            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.themoviedb.org/authenticate/$tokenLogin"))
                            startActivity(intent)
                        }
                    }

                }
            }
        }
    }

    private fun isSessionValid(sessionTime: String): Boolean {
        // Tanggal target sesi dalam format UTC
        val targetDateString = sessionTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Parsing tanggal target
        val targetDate = dateFormat.parse(targetDateString)

        // Mendapatkan waktu saat ini
        val currentDate = Date()

        // Membandingkan apakah waktu saat ini sebelum waktu target
        return currentDate.before(targetDate)
    }

    override fun onResume() {
        super.onResume()
        val tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
        if (tokenValidate.isNullOrEmpty() && tokenExp != null){
            sharedPreferences.edit().putString(Constant.REQUEST_TOKEN_VALIDATE, tokenExp).apply()
        }
    }
}