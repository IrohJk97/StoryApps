package com.musyarrofah.storyapps.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.databinding.ActivityRegisterBinding
import com.musyarrofah.storyapps.repository.ResultProcess
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
        binding.tvLogin.setOnClickListener{
            moveToLoginActivity()
        }
        signUp()
        playAnimation()
    }

    private fun signUp() {
        binding.btnRegister.setOnClickListener{
            if (valid()) {
                val name = binding.edtName.text.toString()
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                registerViewModel.userRegister(name, email, password).observe(this) {
                    when (it) {
                        is ResultProcess.Success -> {
                            showLoading(false)
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            moveToLoginActivity()
                        }
                        is ResultProcess.Loading -> showLoading(true)
                        is ResultProcess.Error -> {
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
    }

    private fun moveToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun valid() =
        binding.edtEmail.error == null
                && binding.edtPassword.error == null
                && binding.edtName.error == null
                && !binding.edtEmail.text.isNullOrEmpty()
                && !binding.edtPassword.text.isNullOrEmpty()
                && !binding.edtName.text.isNullOrEmpty()

    private fun showLoading(state: Boolean) {
        if (state){
            binding.progressCircular.visibility = View.VISIBLE
        }
        else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun playAnimation() {

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