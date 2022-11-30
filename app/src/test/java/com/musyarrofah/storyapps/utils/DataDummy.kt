package com.musyarrofah.storyapps.utils

import com.musyarrofah.storyapps.liststory.StoryResponse

object DataDummy {
    fun generateDummyNewsEntity(): List<StoryResponse.Story> {
        val newsList = ArrayList<StoryResponse.Story>()
        for (i in 0..10) {
            val news = StoryResponse.Story(
                "id",
                "name",
                "description",
                "photo_url",
                ""
            )
            newsList.add(news)
        }
        return newsList
    }
}
