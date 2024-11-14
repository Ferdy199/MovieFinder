package com.ferdsapp.moviefinder.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
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
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.utils.Constant
import com.ferdsapp.moviefinder.databinding.ActivityLoginBinding
import com.ferdsapp.moviefinder.viewModel.login.LoginViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val webViewClients = WebViewClient()

    private var tokenLogin = ""
    private var tokenValidate = ""
    private var tokenExp = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.loginWebView){
            webViewClient = webViewClients
            settings.javaScriptEnabled = true
        }

        //observe token validate
        observeTokenValidate()

        //observe request token
        observeRequestToken()

        binding.loginButton.setOnClickListener {
            checkRequestToken()
        }
    }

    private fun observeTokenValidate(){
        loginViewModel.getTokenValidate.observe(this) { getTokenValidate ->
            when (getTokenValidate) {
                is ApiResponse.Success -> {
                    tokenValidate = getTokenValidate.data
                }
                is ApiResponse.Empty -> {
                    getTokenLogin()
                }
                is ApiResponse.Error -> {
                    Log.d("Login Activity", "response error: ${getTokenValidate.errorMessage}")
                    showCustomSnackbar(binding.root, getTokenValidate.errorMessage)
                }
            }
        }
    }

    private fun observeRequestToken(){
        loginViewModel.getRequestToken.observe(this){ requestToken ->
            tokenLogin = requestToken
        }
    }

    private fun getTokenLogin(){
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
                    showCustomSnackbar(binding.root, apiResponse.errorMessage)
                }
            }
        }
    }

    private fun checkRequestToken(){
        var sessionValid = false
        if (tokenValidate.isEmpty()){
            binding.loginWebView.visibility = WebView.VISIBLE
            binding.loginCard.visibility = CardView.GONE
            binding.loginWebView.loadUrl("https://www.themoviedb.org/authenticate/$tokenLogin")
            handleBackPressed()
        }else{
            loginViewModel.getSessionInvalid(tokenValidate).observe(this) { key ->
                sessionValid = key
            }
            checkSessionValid(sessionValid)
        }
    }

    private fun  checkSessionValid(sessionValid: Boolean){
        when(sessionValid){
            true -> {
                Toast.makeText(this, "Session Valid", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if (tokenLogin.isEmpty()){
                    Toast.makeText(this, "Token Kosong", Toast.LENGTH_SHORT).show()
                }else{
                    binding.loginWebView.visibility = WebView.VISIBLE
                    binding.loginCard.visibility = CardView.GONE
                    binding.loginWebView.loadUrl("https://www.themoviedb.org/authenticate/$tokenLogin")
                    handleBackPressed()
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

    private fun handleBackPressed(){
        onBackPressedDispatcher.addCallback(this){
            binding.loginCard.visibility = CardView.VISIBLE
            binding.loginWebView.visibility = WebView.GONE
            handleRequestToken()
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
        snackbar.setText("") // Hilangkan teks default
        snackbar.view.setPadding(0, 0, 0, 0) // Hilangkan padding default Snackbar
        (snackbar.view as ViewGroup).addView(customview) // Tambahkan custom view
        snackbar.show()
    }
}