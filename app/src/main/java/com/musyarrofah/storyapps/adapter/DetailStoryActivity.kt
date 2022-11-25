package com.musyarrofah.storyapps.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.musyarrofah.storyapps.databinding.ActivityDetailStoryBinding
import com.musyarrofah.storyapps.liststory.StoryResult

class DetailStoryActivity : AppCompatActivity() {
    companion object {
        const val DETAIL_STORY = "detail_story"
    }


    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<StoryResult>(DETAIL_STORY)

        binding.tvName.text = data?.name
        binding.isiDescription.text = data?.description
        Glide.with(this)
            .load(data?.photoUrl)
            .apply(RequestOptions().override(300, 300))
            .into(binding.photoDetails)

    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}