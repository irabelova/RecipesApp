package com.example.recipes

import com.example.recipes.data.network.RecipeApiService
import com.example.recipes.data.network.dto.RandomRecipeDto
import com.example.recipes.data.network.dto.RecipeListDto
import com.example.recipes.data.network.dto.VideoListDto
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.mappers.VideoDtoMapper
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class RapidApiSourceUnitTests {
    @Mock
    private lateinit var retrofitService: RecipeApiService
    @Mock
    private lateinit var recipeDtoMapper: RecipeDtoMapper
    @Mock
    private lateinit var videoDtoMapper: VideoDtoMapper


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    private fun createRapidApiSource() =
        RapidApiSource(retrofitService, recipeDtoMapper, videoDtoMapper)


    @Test
    fun `get random recipe test`() = runTest {
        val dto = RandomRecipeDto(listOf(recipeDto))
        Mockito.`when`(retrofitService.getRandomRecipe())
            .thenReturn(RandomRecipeDto(listOf(recipeDto)))
        Mockito.`when`(recipeDtoMapper.randomRecipeDtoToRecipes(dto)).thenReturn(listOf(defaultRecipe))
        val rapidApiSource = createRapidApiSource()
        val result = rapidApiSource.getRandomRecipe()
        verify(retrofitService, times(1)).getRandomRecipe()
        verify(recipeDtoMapper, times(1)).randomRecipeDtoToRecipes(dto)
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `get recipe by id test`() = runTest {
        Mockito.`when`(retrofitService.getRecipe(defaultRecipe.id)).thenReturn(recipeDto)
        Mockito.`when`(recipeDtoMapper.recipeDtoToRecipe(recipeDto)).thenReturn(defaultRecipe)
        val rapidApiSource = createRapidApiSource()
        val result = rapidApiSource.getRecipeById(defaultRecipe.id)
        verify(retrofitService, times(1)).getRecipe(defaultRecipe.id)
        verify(recipeDtoMapper, times(1)).recipeDtoToRecipe(recipeDto)
        TestCase.assertEquals(defaultRecipe, result)
    }

    @Test
    fun `get recipe by request test`() = runTest {
        Mockito.`when`(retrofitService.getRecipeByRequest(title = defaultRecipe.title)).thenReturn(
            RecipeListDto(listOf(recipeByRequestDto))
        )
        Mockito.`when`(retrofitService.getRecipe(recipeByRequestDto.id)).thenReturn(
            recipeDto
        )
        Mockito.`when`(recipeDtoMapper.recipeDtoToRecipe(recipeDto)).thenReturn(defaultRecipe)
        val rapidApiSource = createRapidApiSource()
        val result = rapidApiSource.getRecipeByRequest(defaultRecipe.title)
        verify(retrofitService, times(1)).getRecipeByRequest(title = defaultRecipe.title)
        verify(recipeDtoMapper, times(1)).recipeDtoToRecipe(recipeDto)
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `get video list test`() = runTest {
        val dto = VideoListDto(listOf(videoDto))
        Mockito.`when`(retrofitService.getVideoList(title = video.title)).thenReturn(
            dto
        )
        Mockito.`when`(videoDtoMapper.videoListDtoToVideoList(dto)).thenReturn(
            listOf(video)
        )
        val rapidApiSource = createRapidApiSource()
        val result = rapidApiSource.getVideoList(video.title)
        verify(retrofitService, times(1)).getVideoList(title = video.title)
        verify(videoDtoMapper, times(1)).videoListDtoToVideoList(dto)
        TestCase.assertEquals(listOf(video), result)
    }
}