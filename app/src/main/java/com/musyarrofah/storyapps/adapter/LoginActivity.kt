package com.musyarrofah.storyapps.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.databinding.ActivityLoginBinding
import com.musyarrofah.storyapps.login.LoginRequest
import com.musyarrofah.storyapps.preferences.SettingPreference
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.viewmodel.LoginViewModel
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel
import com.musyarrofah.storyapps.viewmodel.UserViewModel

class LoginActivity(repository: StoryRepository) : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel

    // ViewModel
    private lateinit var viewModel: UserViewModel
    private lateinit var prefViewModel: PreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        factory = ViewModelFactory.getInstance(this)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

//        val pref = SettingPreference.getInstance(dataStore)
//        prefViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]

        binding.tvRegister.setOnClickListener {
            intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
            prefViewModel.saveLoginState(true)
        }

        viewModel.loginResponse.observe(this) {
            if (it != null){
                showLoading(true)
                intent = Intent(this, MainActivity::class.java)
                prefViewModel.saveBearerToken(it.loginResult?.token!!)
                startActivity(intent)
                finish()
            }
        }

        playAnimation()

    }

    private fun login(){
        val email = binding.edtEmail.text.toString()?.trim()
        val password = binding.edtPassword.text?.toString()?.trim()
        viewModel.userLogin(LoginRequest(email, password))
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun playAnimation(){

        // Title dan SubTitle
        val titleLogin = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(500)
        val subtitleLogin = ObjectAnimator.ofFloat(binding.tvLoginSubTitle, View.ALPHA, 1f).setDuration(500)

        // Email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)

        // Password
        val textPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val inputPassword = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)

        // Button
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val regisLayout = ObjectAnimator.ofFloat(binding.layoutRegister, View.ALPHA, 1f).setDuration(500)

        val setEmail = AnimatorSet().apply {
            playTogether(textEmail, inputEmail)
        }

        val setPassword = AnimatorSet().apply {
            playTogether(textPassword, inputPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                titleLogin,
                subtitleLogin,
                setEmail,
                setPassword,
                btnLogin,
                regisLayout
            )
            start()
        }
    }


}