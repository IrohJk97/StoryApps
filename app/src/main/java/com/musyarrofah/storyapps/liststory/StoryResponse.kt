package com.musyarrofah.storyapps.liststory

data class StoryResponse(
    val error: Boolean? = null,
    val message: String = "",
    val listStory: List<StoryResult>? = null
)