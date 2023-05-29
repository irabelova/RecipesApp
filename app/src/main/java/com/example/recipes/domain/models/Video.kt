package com.example.recipes.domain.models

data class Video (
    val title: String = "chicken",
    val youTubeId: String,
    val thumbnail: String,
) {
    val url = "https://www.youtube.com/watch?v=${youTubeId}"
}