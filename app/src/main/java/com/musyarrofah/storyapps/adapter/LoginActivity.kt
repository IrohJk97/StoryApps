package com.musyarrofah.storyapps.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.databinding.ActivityLoginBinding
import com.musyarrofah.storyapps.liststory.UserModel
import com.musyarrofah.storyapps.utils.Result
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        login()
        playAnimation()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            if (valid()) {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()

                loginViewModel.userLogin(email, password).observe(this) {
                    when (it) {
                        is Result.Success -> {
                            showLoading(false)
                            val response = it.data
                            saveUserData(
                                UserModel(
                                    response.loginResult?.name.toString(),
                                    response.loginResult?.token.toString(),
                                    true
                                )
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }
                        is Result.Loading -> showLoading(true)
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Periksa Masukan Anda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun saveUserData(user: UserModel) {
        loginViewModel.saveUser(user)
    }

    private fun showLoading(state: Boolean) {
        if (state){
            binding.progressCircular.visibility = View.VISIBLE
        }
        else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun valid() =
        binding.edtEmail.error == null && binding.edtPassword.error == null && !binding.edtEmail.text
            .isNullOrEmpty() && !binding.edtPassword.text.isNullOrEmpty()

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