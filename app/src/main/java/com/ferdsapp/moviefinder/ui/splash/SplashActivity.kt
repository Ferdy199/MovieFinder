package com.ferdsapp.moviefinder.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.ui.login.LoginActivity
import com.ferdsapp.moviefinder.ui.main.MainActivity
import com.ferdsapp.moviefinder.viewModel.splash.SplashViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var session_token_login = ""

        splashViewModel.getTokenLogin().observe(this){ tokenLogin ->
            Log.d("splash_Activity", "session_token_login1: $tokenLogin ")
            session_token_login = tokenLogin

            Log.d("splash_Activity", "session_token_login2: $session_token_login ")

            if (session_token_login.isEmpty() || session_token_login == "failed"){
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }else{
                splashViewModel.getSessionInvalid(session_token_login).observe(this){ session ->
                    when(session){
                        true -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            }

                        }
                        false -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(2000)
                                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }

    }
}