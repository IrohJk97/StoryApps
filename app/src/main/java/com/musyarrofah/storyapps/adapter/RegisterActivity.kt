package com.musyarrofah.storyapps.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.databinding.ActivityRegisterBinding
import com.musyarrofah.storyapps.register.RegisterRequest
import com.musyarrofah.storyapps.repository.StoryRepository
import com.musyarrofah.storyapps.viewmodel.UserViewModel

class RegisterActivity(repository: StoryRepository) : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.tvLogin.setOnClickListener{
            moveToLoginActivity()
        }

        binding.btnRegister.setOnClickListener {
            signUp()
        }

        viewModel.registerResponse.observe(this@RegisterActivity) {
            if (it.error == false){
                showLoading(false)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                moveToLoginActivity()
                Log.d("Result: ", it.message)
            } else {
                showLoading(false)
                Log.d("Result: ", it.message)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        playAnimator()

    }

    private fun moveToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        viewModel.userRegister(
            RegisterRequest(name, email, password)
        )
        showLoading(true)
    }

    private fun showLoading(state : Boolean){
        if (state){
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun playAnimator() {

        // Title dan SubTitle
        val titleRegis = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val subtitleRegis = ObjectAnimator.ofFloat(binding.tvRegisterSubTitle, View.ALPHA, 1f).setDuration(500)

        // Name
        val textName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val inputName = ObjectAnimator.ofFloat(binding.tlName, View.ALPHA, 1f).setDuration(500)

        // Email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val inputEmail = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)

        // Password
        val textPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val inputPassword = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)

        // Button
        val btnRegis = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val logLayout = ObjectAnimator.ofFloat(binding.layoutLogin, View.ALPHA, 1f).setDuration(500)

        val setName = AnimatorSet().apply {
            playTogether(textName, inputName)
        }

        val setEmail = AnimatorSet().apply {
            playTogether(textEmail, inputEmail)
        }

        val setPassword = AnimatorSet().apply {
            playTogether(textPassword, inputPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                titleRegis,
                subtitleRegis,
                setName,
                setEmail,
                setPassword,
                btnRegis,
                logLayout
            )
            start()
        }

    }


}