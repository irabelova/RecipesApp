package com.example.recipes.domain.mappers

import com.example.recipes.data.network.dto.VideoDto
import com.example.recipes.data.network.dto.VideoListDto
import com.example.recipes.domain.models.Video

class VideoDtoMapper {

    fun videoListDtoToVideoList(videoDtoList: VideoListDto): List<Video> {
        val videoList = videoDtoList.videos.map {
            videoDtoToVideo(it)
        }
        return videoList
    }

    private fun videoDtoToVideo(videoDto: VideoDto): Video {
        return Video(
            title = videoDto.title,
            thumbnail = videoDto.thumbnail,
            youTubeId = videoDto.youTubeId
        )
    }
}