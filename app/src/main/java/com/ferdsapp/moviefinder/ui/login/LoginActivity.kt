package com.ferdsapp.moviefinder.ui.login

import android.content.Intent
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
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var tokenLogin = ""
    private var tokenValidate = ""
    private var tokenExp = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.getTokenValidate.observe(this) { getTokenValidate ->
            when (getTokenValidate) {
                is ApiResponse.Success -> {
                    tokenValidate = getTokenValidate.data
                }
                is ApiResponse.Empty -> {
                    loginViewModel.getTokenLogin.observe(this) { apiResponse ->
                        when (apiResponse) {
                            is ApiResponse.Success -> {
                                tokenExp = apiResponse.data.expires_at
                                tokenLogin = apiResponse.data.request_token
                            }
                            is ApiResponse.Empty -> {
                                Log.d("Login Activity", "response error: empty")
                            }
                            is ApiResponse.Error -> {
                                Log.d("Login Activity", "response error: ${apiResponse.errorMessage}")
                            }
                        }
                    }
                }
                is ApiResponse.Error -> {
                    Log.d("Login Activity", "response error: ${getTokenValidate.errorMessage}")
                }
            }
        }

        loginViewModel.getRequestToken.observe(this){ requestToken ->
            tokenLogin = requestToken
        }

        binding.loginButton.setOnClickListener {
            var sessionValid = false
            if (tokenValidate.isEmpty()){
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.themoviedb.org/authenticate/$tokenLogin"))
                startActivity(intent)
            }else{
                 loginViewModel.getSessionInvalid(tokenValidate).observe(this) { key ->
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
        val sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)
        if (tokenValidate.isEmpty() && tokenExp.isNotEmpty()){
            loginViewModel.saveTokenValidate(tokenExp)
            tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
        }
    }
}