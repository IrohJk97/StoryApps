package com.musyarrofah.storyapps.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.musyarrofah.storyapps.R
import com.musyarrofah.storyapps.databinding.ActivityCreateStoryBinding
import com.musyarrofah.storyapps.utils.Result
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.utils.reduceFileImage
import com.musyarrofah.storyapps.utils.rotateBitmap
import com.musyarrofah.storyapps.utils.uriToFile
import com.musyarrofah.storyapps.viewmodel.CreateStoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var createStoryViewModel: CreateStoryViewModel
    private var getFile: File? = null
    private var result: Bitmap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        const val GALLERY_RESULT = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!cameraPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            if (cameraPermissionsGranted()) {
                startCameraX()
            }
        }
    }

    private fun cameraPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Create Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        factory = ViewModelFactory.getInstance(this)
        createStoryViewModel = ViewModelProvider(this, factory)[CreateStoryViewModel::class.java]
        val resultCode = intent.getIntExtra("resultCode", 0)
        if (resultCode == GALLERY_RESULT) {
            startGallery()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.btnCamera.setOnClickListener {
            accessCamera()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {
            createStory()
        }
        binding.btnSwitch.setOnClickListener {
            getMyLocation()
        }
    }

    private fun accessCamera() {
        if (!cameraPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startCameraX()
        }
    }


    private fun createStory() {
        createStoryViewModel.getUser().observe(this@CreateStoryActivity) {
            val token = "Bearer " + it.token
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val description =
                    "${binding.edtDescription.text}".toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                createStoryViewModel.getUser().observe(this@CreateStoryActivity) {
                    createStoryViewModel.addStory(token, imageMultipart, description, lat, lon)
                        .observe(this@CreateStoryActivity) {
                            when (it) {
                                is Result.Success -> {
                                    showLoading(false)
                                    startActivity(
                                        Intent(
                                            this@CreateStoryActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    Toast.makeText(this, "Upload Succes", Toast.LENGTH_SHORT).show()
                                    finish()

                                }
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Error -> {
                                    Toast.makeText(
                                        this@CreateStoryActivity,
                                        it.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                }
            } else {
                Toast.makeText(
                    this@CreateStoryActivity,
                    getString(R.string.input_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    @Suppress("DEPRECATION")
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
                rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )
        }
        binding.previewImageView.setImageBitmap(result)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    @Suppress("DEPRECATION")
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateStoryActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    Toast.makeText(
                        this,
                        "Saved Location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Tidak ada lokasi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            (Manifest.permission.ACCESS_COARSE_LOCATION)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return true
    }

}