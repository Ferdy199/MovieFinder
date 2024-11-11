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
import com.ferdsapp.moviefinder.databinding.ActivityLoginBinding
import com.ferdsapp.moviefinder.viewModel.login.LoginViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
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
        sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)

//        val reqToken = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
       loginViewModel.getTokenValidate.observe(this){
            tokenValidate = it
        }

        if (tokenValidate.isEmpty()){
            loginViewModel.getTokenLogin.observe(this) { apiResponse ->
                when(apiResponse){
                    is ApiResponse.Success -> {
                        tokenExp = apiResponse.data.expires_at
//                        tokenLogin = apiResponse.data.request_token.ifEmpty {
//                            sharedPreferences.getString(Constant.REQUEST_TOKEN, "").toString()
//                        }
                        Log.d("Login Activity", "tokenExp : $tokenExp")
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

        loginViewModel.getRequestToken.observe(this){ requestToken ->
            Log.d("Login Activity", "onCreate: requestToken $requestToken")
            tokenLogin = requestToken
        }

        loginViewModel.getTokenValidate.observe(this){ token ->
            Log.d("Login Activity", "onCreate: tokenValidate $token")
            tokenValidate = token
        }


        binding.loginButton.setOnClickListener {
//            val tokenValidate = sharedPreferences.getString(Constant.REQUEST_TOKEN_VALIDATE, "").toString()
            Log.d("Login Activity", "onButtonClick: tokenValidate value $tokenValidate")
            var sessionValid = false
            if (tokenValidate.isEmpty()){
                Log.d("Login Activity", "onbuttonClick: 1st condition")
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.themoviedb.org/authenticate/$tokenLogin"))
                startActivity(intent)
            }else{
                Log.d("Login Activity", "onbuttonClick: 2nd condition")
                 loginViewModel.getSessionInvalid(tokenValidate!!).observe(this) { key ->
                     sessionValid = key
                }
                when(sessionValid){
                    true -> {
                        Log.d("Login Activity", "onbuttonClick: sessionValid")
                        Toast.makeText(this, "Session Valid", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.d("Login Activity", "onbuttonClick: sessionInvalid")
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
        loginViewModel.getTokenValidate.observe(this){
            tokenValidate = it
        }
        Log.d("LoginActivity", "onResume: $tokenValidate , $tokenExp")
        if (tokenValidate.isEmpty() && tokenExp.isEmpty()){
            Log.d("LoginActivity", "onResume: saveTokenExp")
            loginViewModel.saveTokenValidate(tokenExp)
//            sharedPreferences.edit().putString(Constant.REQUEST_TOKEN_VALIDATE, tokenExp).apply()
        }
    }
}