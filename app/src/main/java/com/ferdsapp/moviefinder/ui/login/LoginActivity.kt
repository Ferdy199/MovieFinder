package com.ferdsapp.moviefinder.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.core.utils.Constant
import com.ferdsapp.moviefinder.databinding.ActivityLoginBinding
import com.ferdsapp.moviefinder.ui.main.MainActivity
import com.ferdsapp.moviefinder.viewModel.login.LoginViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    private val webViewClients = WebViewClient()

    private var tokenLogin = ""
    private var tokenValidate = ""
    private var tokenExp = ""
    private var sessionValid = false


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginWebView.apply {
            webViewClient = webViewClients
            settings.apply {
                javaScriptEnabled = true
                allowFileAccess = false
                domStorageEnabled = true
            }
        }

        binding.signUp.setOnClickListener {
            binding.loginWebView.visibility = WebView.VISIBLE
            binding.loginCard.visibility = CardView.GONE
            binding.signUpMessage.visibility = View.GONE
            binding.loginLogo.visibility = View.GONE
            binding.loginWebView.loadUrl("https://www.themoviedb.org/signup")
            handleBackPressed(false)
        }

        //observe token validate
        observeTokenValidate()

        //observe request token
//        observeRequestToken()

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            checkRequestToken(username, password)
        }
    }

    private fun observeTokenValidate(){
        val sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)
        loginViewModel.getTokenValidate.observe(this) { getTokenValidate ->
            when (getTokenValidate) {
                is Resource.Success -> {
                    Log.d("Login Activity", "observeTokenValidate Success: ${getTokenValidate.data}")
                    loginViewModel.getSessionInvalid(getTokenValidate.data).observe(this){ session ->
                        Log.d("Login Activity", "observeTokenValidate: $session")
                        when(session){
                            true -> {
                                tokenValidate = getTokenValidate.data
                            }
                            false -> {
                                sharedPreferences.edit().remove(Constant.SESSION_REQUEST_TOKEN_VALIDATE).apply()
                                getTokenLogin()
                            }
                        }

                    }
                }
                is Resource.Empty -> {
                    Log.d("Login Activity", "observeTokenValidate Empty")
                    binding.loadingAnimation.visibility = View.GONE
                    getTokenLogin()
                }
                is Resource.Error -> {
                    Log.d("Login Activity", "observeTokenValidate error: ${getTokenValidate.message}")
                    binding.loadingAnimation.visibility = View.GONE
                    showCustomSnackbar(binding.root, getTokenValidate.message)
                }

                is Resource.Loading -> {
//                    binding.loginCard.visibility = View.GONE
                    binding.loadingAnimation.visibility = View.VISIBLE
                }
            }
        }
    }

//    private fun observeRequestToken(){
//        loginViewModel.getRequestToken.observe(this){ requestToken ->
//            tokenLogin = requestToken
//        }
//    }

    private fun getTokenLogin(){
        loginViewModel.getRequestToken.observe(this) { apiResponse ->
            when (apiResponse) {
                is Resource.Success -> {
                    Log.d("Login Activity", "getTokenLogin : success")
                    binding.loadingAnimation.visibility = View.GONE
                    tokenExp = apiResponse.data.expires_at
                    tokenLogin = apiResponse.data.request_token
                }
                is Resource.Empty -> {
                    Log.d("Login Activity", "getTokenLogin : empty")
                    binding.loadingAnimation.visibility = View.GONE
                }
                is Resource.Error -> {
                    Log.d("Login Activity", "getTokenLogin error: ${apiResponse.message}")
                    binding.loadingAnimation.visibility = View.GONE
                    showCustomSnackbar(binding.root, apiResponse.message)
                }
                is Resource.Loading -> {
//                    binding.loginCard.visibility = View.GONE
                    binding.loadingAnimation.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkRequestToken(username: String, password: String){
        Log.d("Login Activity", "tokenValidate on checkRequest Token: $tokenValidate")
        if (tokenValidate.isEmpty()){
            Log.d("Login Activity", "checkRequestToken: first condition")
            binding.loginWebView.visibility = WebView.VISIBLE
            binding.loginCard.visibility = CardView.GONE
            binding.signUpMessage.visibility = View.GONE
            binding.loginLogo.visibility = View.GONE
            binding.loginWebView.loadUrl("https://www.themoviedb.org/authenticate/$tokenLogin")
            handleBackPressed(true)
        }else{
            Log.d("Login Activity", "checkRequestToken: second condition token: $tokenValidate")
            loginViewModel.getSessionInvalid(tokenValidate).observe(this) { key ->
                Log.d("Login Activity", "getSessionInvalid: $key")
                sessionValid = key

                Log.d("Login Activity", "checkRequestToken: sessionValid $sessionValid")
                checkSessionValid(sessionValid, username, password)
            }
        }
    }

    private fun  checkSessionValid(sessionValid: Boolean, username: String, password: String){
        when(sessionValid){
            true -> {
                loginViewModel.loginProcess(username = username, password = password).observe(this){ apiResponse ->
                    when(apiResponse){
                        is Resource.Empty -> {
                            Log.d("Login Activity", "checkSessionValid Empty")
                            binding.loadingAnimation.visibility = View.GONE
                            showCustomSnackbar(binding.root, "response Empty")
                        }
                        is Resource.Error -> {
                            Log.d("Login Activity", "checkSessionValid error: ${apiResponse.message}")
                            binding.loadingAnimation.visibility = View.GONE
                            showCustomSnackbar(binding.root, apiResponse.data?.expires_at ?: apiResponse.data?.status_message ?: "unknown Error")
                        }
                        is Resource.Success -> {
                            binding.loadingAnimation.visibility = View.GONE
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        is Resource.Loading -> {
//                            binding.loginCard.visibility = View.GONE
                            binding.loadingAnimation.visibility = View.VISIBLE
                        }
                        else -> {
                            showCustomSnackbar(binding.root, "unknown Error")
                            Log.d("Login Activity", "unknown Error")
                        }
                    }
                }
            }
            else -> {
                if (tokenLogin.isEmpty()){
                    Toast.makeText(this, "Token Kosong", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Login Activity", "checkRequestToken: tokenLogin $tokenLogin")
                    binding.loginWebView.visibility = WebView.VISIBLE
                    binding.loginCard.visibility = CardView.GONE
                    binding.loginWebView.loadUrl("https://www.themoviedb.org/authenticate/$tokenLogin")
                    handleBackPressed(true)
                }
            }
        }
    }

    private fun handleRequestToken(){
        val sharedPreferences = getSharedPreferences("MovieFinder", MODE_PRIVATE)
        if (tokenValidate.isEmpty() && tokenExp.isNotEmpty()){
            loginViewModel.saveTokenValidate(tokenExp)
            tokenValidate = sharedPreferences.getString(Constant.SESSION_REQUEST_TOKEN_VALIDATE, "").toString()
        }
    }

    private fun handleBackPressed(isRequestToken: Boolean){
        onBackPressedDispatcher.addCallback(this){
            binding.apply {
                loginCard.visibility = CardView.VISIBLE
                loginLogo.visibility = View.VISIBLE
                signUpMessage.visibility = View.VISIBLE
                loginWebView.visibility = View.GONE
            }
            if (isRequestToken) handleRequestToken()
        }
    }

    private fun showCustomSnackbar(view: View, message: String){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)

        val customview by lazy {
            LayoutInflater.from(view.context).inflate(R.layout.custom_snackbar, null).apply {
                findViewById<TextView>(R.id.snackbar_text).text = message
                Glide.with(view.context)
                    .load(R.drawable.logo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(4)))
                    .into(this.findViewById(R.id.snackbar_icon))
            }
        }

        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.white))
        snackbar.setText("")
        snackbar.view.setPadding(0, 0, 0, 0)
        (snackbar.view as ViewGroup).addView(customview)
        snackbar.show()
    }
}