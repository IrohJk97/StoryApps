package com.musyarrofah.storyapps.paging

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musyarrofah.storyapps.adapter.DetailStoryActivity
import com.musyarrofah.storyapps.databinding.ItemStoryBinding
import com.musyarrofah.storyapps.liststory.StoryResponse

class StoryPagingAdapter: PagingDataAdapter<StoryResponse.Story, StoryPagingAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryResponse.Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(storyPhoto)
                tvUsername.text = story.name
                tvDescription.text = story.description
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.DETAIL_STORY, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyPhoto, "image"),
                        Pair(binding.tvUsername, "name"),
                        Pair(binding.tvDescription,"deskripsi")
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse.Story>() {
            override fun areItemsTheSame(oldItem: StoryResponse.Story, newItem: StoryResponse.Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponse.Story, newItem: StoryResponse.Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}