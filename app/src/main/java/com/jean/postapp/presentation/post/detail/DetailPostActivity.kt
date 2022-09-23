package com.jean.postapp.presentation.post.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jean.postapp.R
import com.jean.postapp.databinding.ActivityDetailPostBinding
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.presentation.post.detail.adapter.CommentAdapter
import com.jean.postapp.presentation.post.detail.state.PostDetailUiState
import com.jean.postapp.presentation.post.detail.viewmodel.PostDetailViewModel
import com.jean.postapp.util.Constant.EXTRA_POST_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding

    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter
    private var postId = 0
    private var isFavorite = false
    private var post: Post? = null
    private var favoriteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        setupListeners()
        collectUiState()
        getIntentExtra()
        viewModel.getPostDetail(postId)
    }

    private fun setupRecycler() {
        commentAdapter = CommentAdapter()
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(
                this@DetailPostActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = commentAdapter
            addItemDecoration(DividerItemDecoration(
                this@DetailPostActivity,
                LinearLayoutManager.VERTICAL
            ))
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.btnRetryDetailPost.setOnClickListener {
            viewModel.getPostDetail(postId)
        }
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
    }

    fun handleUiState(uiState: PostDetailUiState) {
        binding.progressBarPostDetail.isVisible = uiState.isLoading

        val hasPostData = uiState.post != null
        val hasComments = !uiState.post?.comments.isNullOrEmpty()
        val hasAuthorInfo = uiState.post?.author != null

        isFavorite = uiState.post?.isFavorite ?: false
        favoriteItem?.setIcon(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        post = uiState.post

        binding.linearDetail.isVisible = hasPostData
        if (hasPostData) {
            binding.tvTitle.text = uiState.post?.title
            binding.tvDescription.text = uiState.post?.body
        }

        binding.rvComments.isVisible = hasComments
        binding.tvErrorComment.isVisible = !hasComments
        if (hasComments) commentAdapter.submitList(uiState.post?.comments)

        binding.linearAuthorInfo.isVisible = hasAuthorInfo
        binding.tvErrorAuthor.isVisible = !hasAuthorInfo
        if (hasAuthorInfo) {
            binding.tvAuthorName.text = uiState.post?.author?.name
            binding.tvAuthorEmail.text = uiState.post?.author?.email
            binding.tvAuthorPhone.text = uiState.post?.author?.phone
            binding.tvAuthorWebsite.text = uiState.post?.author?.website
        }

        val hasData = uiState.post != null
        val hasError = uiState.errorMessage.isNotEmpty()
        binding.linearDetailEmpty.isVisible = !hasData && hasError
        binding.tvErrorDetailMessage.text = uiState.errorMessage
    }

    private fun getIntentExtra() {
        intent.extras?.let {
            postId = it.getInt(EXTRA_POST_ID)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        favoriteItem = menu?.findItem(R.id.icon_favorite)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icon_favorite -> {
                if (isFavorite) {
                    post?.let {
                        viewModel.removeFavorite(it)
                        favoriteItem?.setIcon(R.drawable.ic_star_border)
                        Snackbar.make(
                            binding.root,
                            getString(R.string.remove_post_from_favorite),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    true
                } else {
                    post?.let {
                        viewModel.insertFavorite(it)
                        favoriteItem?.setIcon(R.drawable.ic_star)
                        Snackbar.make(
                            binding.root,
                            getString(R.string.add_post_to_favorite),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() { }
}