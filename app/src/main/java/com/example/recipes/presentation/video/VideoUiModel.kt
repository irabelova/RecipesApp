package com.example.recipes.presentation.video

import com.example.recipes.domain.models.Video

sealed interface VideoUiModel {
    object Loading : VideoUiModel
    data class Data(val videos: List<Video>) : VideoUiModel
    object Error : VideoUiModel
}