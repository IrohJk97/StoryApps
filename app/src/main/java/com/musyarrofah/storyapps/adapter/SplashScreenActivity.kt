package com.musyarrofah.storyapps.adapter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.R
import com.musyarrofah.storyapps.preferences.SettingPreference
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel
import com.musyarrofah.storyapps.viewmodel.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

    private lateinit var splashScreenViewModel: SplashScreenViewModel

    companion object{
        const val DELAY = 4000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

//        val pref = SettingPreference.getInstance(dataStore)
//        setViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            splashScreenViewModel.getUser().observe(this@SplashScreenActivity){
                if (it.isLogin){
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()}, DELAY)

    }
}