package com.example.onboardingassignment.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.onboardingassignment.data.PostModel
import com.example.onboardingassignment.databinding.PostHolderBinding

class PostAdapter(private val onClick: (post: PostModel) -> Unit): ListAdapter<PostModel, ViewHolder>(object: DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PostItem(PostHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        val currentHolder = holder as PostItem

        currentHolder.binding.root.setOnClickListener{
            onClick(post)
        }

        currentHolder.title.text = post.title
        currentHolder.writer.text = post.writer
    }

    class PostItem(val binding: PostHolderBinding): ViewHolder(binding.root) {
        val title = binding.tvTitle
        val writer = binding.tvWriter
    }
}