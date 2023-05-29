package com.example.recipes.presentation.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipes.R
import com.example.recipes.RecipeApplication
import com.example.recipes.data.network.RecipeApi.retrofitService
import com.example.recipes.databinding.VideoFragmentBinding
import com.example.recipes.domain.RapidApiSource
import com.example.recipes.domain.RecipeDataSource
import com.example.recipes.domain.Repository
import com.example.recipes.domain.mappers.RecipeDbMapper
import com.example.recipes.domain.mappers.RecipeDtoMapper
import com.example.recipes.domain.mappers.VideoDtoMapper

class VideoFragment : Fragment() {
    private lateinit var binding: VideoFragmentBinding
    private val viewModel: VideoViewModel by viewModels {
        VideoViewModel.VideoFactory(
            Repository(
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper()),
                RecipeDataSource(
                    (activity?.application as RecipeApplication).database.recipeDao(),
                    RecipeDbMapper()),
                RapidApiSource(retrofitService, RecipeDtoMapper(), VideoDtoMapper())
            )
//            Repository(
//                FakeDataSource(), RecipeDataSource(
//                    (activity?.application as RecipeApplication).database.recipeDao(),
//                    RecipeDbMapper()
//                ), FakeDataSource()
//            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = VideoFragmentBinding.inflate(inflater, container, false)
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setTitle(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = VideoAdapter {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                VideoUiModel.Loading -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                is VideoUiModel.Data -> {
                    adapter.submitList(it.videos)
                    binding.statusContainer.visibility = View.GONE
                    binding.videosRecycleView.adapter = adapter
                }
                VideoUiModel.Error -> {
                    binding.statusContainer.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
            }
        }
    }
}