package com.musyarrofah.storyapps.adapter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musyarrofah.storyapps.R
import com.musyarrofah.storyapps.databinding.ActivityMainBinding
import com.musyarrofah.storyapps.liststory.StoryResult
import com.musyarrofah.storyapps.paging.LoadingStateAdapter
import com.musyarrofah.storyapps.paging.StoryPagingAdapter
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.viewmodel.LoginViewModel
import com.musyarrofah.storyapps.viewmodel.MainActivityViewModel
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel

//class MainActivity : AppCompatActivity() {
//
////    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var mainActivityViewModel: MainActivityViewModel
//    private lateinit var loginViewModel: LoginViewModel
//    private lateinit var prefViewModel: PreferencesViewModel
//    private lateinit var adapter : ListStoryAdapter
//    private lateinit var factory: ViewModelFactory
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        supportActionBar?.title = "List Story"
//
//        factory = ViewModelFactory.getInstance(this)
//
//        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
//        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
////        val pref = SettingPreference.getInstance(dataStore)
////        prefViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]
////        prefViewModel.getBearerToken().observe(this){
////            val bearer = "Bearer $it"
////            storyViewModel.getAllStory(bearer)
////            showLoading(true)
////        }
//
//
//        adapter = ListStoryAdapter(repository)
//        adapter.setOnItemClick(object : ListStoryAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: StoryResult, optionsCompat: ActivityOptionsCompat) {
//                intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
//                intent.putExtra(DetailStoryActivity.DETAIL_STORY, data)
//                startActivity(intent, optionsCompat.toBundle())
//            }
//        })
//
//
//        binding.apply {
//            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
//            rvStory.adapter = adapter
//            rvStory.setHasFixedSize(true)
//
//
//            mainActivityViewModel.getStory().observe(this@MainActivity){
//                showLoading(false)
//                adapter.setData(it)
//            }
//        }
//
//
//        binding.btnAddStory.setOnClickListener {
//            intent = Intent(this, CreateStoryActivity::class.java)
//            startActivity(intent)
//        }
//
//
//    }
//
//    private fun showLoading(state: Boolean) {
//        if (state) {
//            binding.progressCircular.visibility = View.VISIBLE
//        } else {
//            binding.progressCircular.visibility = View.GONE
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_home, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
//
//        R.id.change_language -> {
//            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//            true
//        }
//        R.id.logout -> {
//            prefViewModel.saveLoginState(false)
//            prefViewModel.saveBearerToken("")
//            intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//            true
//        }
//        else ->  {
//            super.onOptionsItemSelected(item)
//        }
//
//    }
//
//}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryPagingAdapter
    private lateinit var factory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var listStoryViewModel: MainActivityViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Float? = null
    private var lon: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        setupViewModel()
        storyAdapter = StoryPagingAdapter()

        listStoryViewModel.getUser().observe(this@MainActivity){ user ->
            if (user.isLogin){
                getStory()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        listStoryViewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        loginViewModel= ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun getStory() {
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        listStoryViewModel.getStory().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showLoading(false)
        }
    }

    private fun logout() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.title_info))
            setMessage(getString(R.string.message_logout))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                loginViewModel.logout()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finishAffinity()
            }
            setNegativeButton("tidak") { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, GoogleMapsActivity::class.java))
            }
            R.id.bahasa -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.keluar -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                    Toast.makeText(
                        this,
                        getString(R.string.location_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION)
            (Manifest.permission.ACCESS_COARSE_LOCATION)

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.loading.visibility = View.VISIBLE
        }
        else {
            binding.loading.visibility = View.GONE
        }
    }

    companion object {
        const val KEY_STORY = "story"
    }
}