package com.example.recipes.presentation.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.VideoItemBinding
import com.example.recipes.domain.models.Video

class VideoAdapter(private val clickListener: (Video) -> Unit) :
    ListAdapter<Video, VideoAdapter.VideoViewHolder>(DiffCallback) {

    class VideoViewHolder(
        private val binding: VideoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video, clickListener: (Video) -> Unit) {
            binding.videoTitle.text = video.title
            binding.card.setOnClickListener { clickListener(video) }
            video.thumbnail.let {
                binding.videoThumbnail.load(video.thumbnail) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Video>() {

        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.youTubeId == newItem.youTubeId
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.title == newItem.title && oldItem.thumbnail == newItem.thumbnail
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VideoViewHolder(
            VideoItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.bind(video, clickListener)
    }
}