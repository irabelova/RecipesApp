package com.example.recipes.domain

import com.example.recipes.domain.models.Video

interface VideoDataSource {

    suspend fun getVideoList(title: String): List<Video>
}