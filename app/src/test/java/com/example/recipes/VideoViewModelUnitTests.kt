package com.example.recipes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.recipes.domain.Repository
import com.example.recipes.presentation.video.VideoUiModel
import com.example.recipes.presentation.video.VideoViewModel
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class VideoViewModelUnitTests {

    @Mock
    private lateinit var repository: Repository

    private val videoList = listOf(video)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
    }

    private fun createViewModel() = VideoViewModel(repository)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `collect query flow more then two characters test`() = runTest {
        val query = "test"
        Mockito.`when`(repository.getVideoList(query)).thenReturn(videoList)
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        Mockito.verify(repository, times(1)).getVideoList(query)
        TestCase.assertEquals(VideoUiModel.Data(videoList), viewModel.state.value)
    }

    @Test
    fun `collect query flow less then two characters test`() = runTest {
        val query = "t"
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        Mockito.verify(repository, times(0)).getVideoList(query)
    }

    @Test
    fun `collect query flow with empty query test`() = runTest {
        val query = ""
        Mockito.`when`(repository.getVideoList("Chicken")).thenReturn(videoList)
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        Mockito.verify(repository, times(1)).getVideoList("Chicken")
        TestCase.assertEquals(VideoUiModel.Data(videoList), viewModel.state.value)
    }

    @Test
    fun `get video list with error state test`() = runTest {
        val query = "test"
        Mockito.`when`(repository.getVideoList(query)).thenThrow(RuntimeException())
        val viewModel = createViewModel()
        viewModel.setTitle(query)
        delay(500)
        Mockito.verify(repository, times(1)).getVideoList(query)
        TestCase.assertEquals(VideoUiModel.Error, viewModel.state.value)
    }
}