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

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var tokenExp: String? = null
    private var tokenValidate: String? = null
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)
        var tokenLogin = ""

//        val reqToken = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()

        loginViewModel.getTokenValidate.observe(this){ token ->
            tokenValidate = token
        }

        if (tokenValidate.isNullOrEmpty()){
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
//            val tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
            var sessionValid = false
            if (tokenValidate.isNullOrEmpty()){
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.themoviedb.org/authenticate/$tokenLogin"))
                startActivity(intent)
            }else{
                 loginViewModel.getSessionInvalid(tokenValidate!!).observe(this) { key ->
                     sessionValid = key
                }
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

    override fun onResume() {
        super.onResume()
        val tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
        Log.d("LoginActivity", "onResume: $tokenValidate , $tokenExp")
        if (tokenValidate.isEmpty() && tokenExp != null){
            loginViewModel.saveTokenValidate(tokenValidate)
//            sharedPreferences.edit().putString(Constant.REQUEST_TOKEN_VALIDATE, tokenExp).apply()
        }
    }
}