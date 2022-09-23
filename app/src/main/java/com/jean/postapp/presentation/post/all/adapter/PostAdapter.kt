package com.jean.postapp.presentation.post.all.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jean.postapp.databinding.ItemPostBinding
import com.jean.postapp.domain.model.post.Post

class PostAdapter(
    private val onClick: (Post) -> Unit,
    private val onDelete: (Post, Int) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(itemBinding, onClick, onDelete)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding,
        private val onClick: (Post) -> Unit,
        private val onDelete: (Post, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) = with(binding) {
            tvTitle.text = post.title
            ivFavorite.isVisible = post.isFavorite
            ivBadgeAlert.isVisible = !post.isSeen

            ivRemove.setOnClickListener {
                onDelete.invoke(post, adapterPosition)
            }
            root.setOnClickListener {
                ivBadgeAlert.isVisible = false
                onClick.invoke(post)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}