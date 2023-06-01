package com.example.recipes

import com.example.recipes.domain.BaseDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.SaveDataSource
import com.example.recipes.domain.VideoDataSource
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
internal class RepositoryUnitTests {
    @Mock
    private lateinit var saveDataSource: SaveDataSource

    @Mock
    private lateinit var baseDataSource: BaseDataSource

    @Mock
    private lateinit var videoDataSource: VideoDataSource


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    private fun createRepository() = Repository(baseDataSource, saveDataSource, videoDataSource)


    @Test
    fun `get random recipe from save data source test`() = runTest {
        Mockito.`when`(saveDataSource.getRandomRecipe()).thenReturn(listOf(defaultRecipe))
        val repository = createRepository()
        val result = repository.getRandomRecipe(true)
        verify(saveDataSource, times(1)).getRandomRecipe()
        verify(baseDataSource, times(0)).getRandomRecipe()
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `get random recipe from base data source test`() = runTest {
        Mockito.`when`(baseDataSource.getRandomRecipe()).thenReturn(listOf(defaultRecipe))
        val repository = createRepository()
        val result = repository.getRandomRecipe(false)
        verify(baseDataSource, times(1)).getRandomRecipe()
        verify(saveDataSource, times(0)).getRandomRecipe()
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `get recipe by id with save data source test` () = runTest {
        Mockito.`when`(saveDataSource.getRecipeById(defaultRecipe.id)).thenReturn(defaultRecipe)
        val repository = createRepository()
        val result = repository.getRecipeById(defaultRecipe.id, true)
        verify(saveDataSource, times(1)).getRecipeById(defaultRecipe.id)
        verify(baseDataSource, times(0)).getRecipeById(defaultRecipe.id)
        TestCase.assertEquals(defaultRecipe, result)
    }

    @Test
    fun `get recipe by id with base data source test` () = runTest {
        Mockito.`when`(saveDataSource.getRecipeById(defaultRecipe.id)).thenReturn(null)
        Mockito.`when`(baseDataSource.getRecipeById(defaultRecipe.id)).thenReturn(defaultRecipe)
        val repository = createRepository()
        val result = repository.getRecipeById(defaultRecipe.id, true)
        verify(saveDataSource, times(1)).getRecipeById(defaultRecipe.id)
        verify(baseDataSource, times(1)).getRecipeById(defaultRecipe.id)
        TestCase.assertEquals(defaultRecipe, result)
    }

    @Test
    fun `get recipe by id from db test` () = runTest {
        Mockito.`when`(saveDataSource.getRecipeById(defaultRecipe.id)).thenReturn(defaultRecipe)
        val repository = createRepository()
        val result = repository.getRecipeById(defaultRecipe.id, false)
        verify(saveDataSource, times(1)).getRecipeById(defaultRecipe.id)
        verify(baseDataSource, times(0)).getRecipeById(defaultRecipe.id)
        TestCase.assertEquals(defaultRecipe, result)
    }

    @Test
    fun `get recipe by request with save data source test`() = runTest {
        Mockito.`when`(saveDataSource.getRecipeByRequest(defaultRecipe.title)).thenReturn(listOf(defaultRecipe))
        val repository = createRepository()
        val result = repository.getRecipeByRequest(defaultRecipe.title, true)
        verify(saveDataSource, times(1)).getRecipeByRequest(defaultRecipe.title)
        verify(baseDataSource, times(0)).getRecipeByRequest(defaultRecipe.title)
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `get recipe by request with base data source test`() = runTest {
        Mockito.`when`(baseDataSource.getRecipeByRequest(defaultRecipe.title)).thenReturn(listOf(defaultRecipe))
        val repository = createRepository()
        val result = repository.getRecipeByRequest(defaultRecipe.title, false)
        verify(baseDataSource, times(1)).getRecipeByRequest(defaultRecipe.title)
        verify(saveDataSource, times(0)).getRecipeByRequest(defaultRecipe.title)
        TestCase.assertEquals(listOf(defaultRecipe), result)
    }

    @Test
    fun `save recipe test` () = runTest {
        Mockito.`when`(saveDataSource.saveRecipe(defaultRecipe)).thenReturn(defaultRecipe.id.toLong())
        val repository = createRepository()
        val result = repository.saveRecipe(defaultRecipe)
        verify(saveDataSource, times(1)).saveRecipe(defaultRecipe)
        TestCase.assertEquals(defaultRecipe.id.toLong(), result)
    }

    @Test
    fun `delete recipe test` () = runTest {
        val repository = createRepository()
        repository.deleteRecipe(defaultRecipe)
        verify(saveDataSource, times(1)).deleteRecipe(defaultRecipe)
    }

    @Test
    fun `update recipe test` () = runTest {
        val repository = createRepository()
        repository.updateRecipe(defaultRecipe)
        verify(saveDataSource, times(1)).updateRecipe(defaultRecipe)
    }

    @Test
    fun `get video list test` () = runTest {
        Mockito.`when`(videoDataSource.getVideoList(video.title)).thenReturn(listOf(video))
        val repository = createRepository()
        val result = repository.getVideoList(video.title)
        verify(videoDataSource, times(1)).getVideoList(video.title)
        TestCase.assertEquals(listOf(video), result)
    }
}