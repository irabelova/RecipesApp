package com.example.recipes

import com.example.recipes.data.network.dto.VideoListDto
import com.example.recipes.domain.mappers.VideoDtoMapper
import junit.framework.TestCase
import org.junit.Test

internal class VideoDtoMapperUnitTest {

    private val videoList = listOf(video)
    private val videoDtoList = listOf(videoDto)
    private val videoListDto = VideoListDto(videoDtoList)

    @Test
    fun `video list dto to video list test` () {
        val videoDtoMapper = VideoDtoMapper()
        val result = videoDtoMapper.videoListDtoToVideoList(videoListDto)
        TestCase.assertEquals(videoList, result)
    }
}