package com.musyarrofah.storyapps.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.musyarrofah.storyapps.R
import com.musyarrofah.storyapps.databinding.ActivityAddStoryBinding
import com.musyarrofah.storyapps.preferences.SettingPreference
import com.musyarrofah.storyapps.utils.ViewModelFactory
import com.musyarrofah.storyapps.utils.reduceFileImage
import com.musyarrofah.storyapps.utils.rotateBitmap
import com.musyarrofah.storyapps.utils.uriToFile
import com.musyarrofah.storyapps.viewmodel.PreferencesViewModel
import com.musyarrofah.storyapps.viewmodel.StoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var prefViewModel: PreferencesViewModel

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        val pref = SettingPreference.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]

        storyViewModel.addStory.observe(this){
            if (it != null){
                showLoading(false)
                intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }

        binding.btnCamera.setOnClickListener {
            startCameraX()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

    }

    private var getFile : File? = null

    private fun uploadImage() {
        if (getFile != null) {
            val description = binding.edtDescription.text.toString()
            if (description.isEmpty()) {
                binding.edtDescription.error = resources.getString(R.string.input_message, "Description")
            } else {
                val descrip = description.toRequestBody("text/plain".toMediaType())
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                prefViewModel.getBearerToken().observe(this){ result ->
                    val bearer = "Bearer $result"
                    storyViewModel.addStory(bearer, descrip, imageMultipart)
                    if (result !=null) {
                        showLoading(true)
                        Toast.makeText(this, "Upload Succes", Toast.LENGTH_SHORT).show()
                    } else {
                        showLoading(true)
                        Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT)
                    }

                }

            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK){
            val selectedImg : Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }

    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        if (it.resultCode == CAMERA_X_RESULT){
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean


            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)
        }

    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}