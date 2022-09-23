package com.jean.postapp.presentation.post.all

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jean.postapp.R
import com.jean.postapp.databinding.FragmentAllPostBinding
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.presentation.post.all.adapter.PostAdapter
import com.jean.postapp.presentation.post.all.state.AllPostUiState
import com.jean.postapp.presentation.post.all.viewmodel.AllPostViewModel
import com.jean.postapp.presentation.post.detail.DetailPostActivity
import com.jean.postapp.util.Constant.EXTRA_POST_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPostFragment : Fragment(R.layout.fragment_all_post) {

    private var _binding: FragmentAllPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllPostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupListeners()
        collectUiState()
        viewModel.getAllPost(isRefreshing = false)
    }

    private fun setupRecycler() {
        postAdapter = PostAdapter(
            onClick = { post ->
                viewModel.insertSeen(post)
                navigateToDetail(post.id)
            },
            onDelete = { post, position ->
                showItemDeleteDialog(post, position)
            }
        )
        binding.rvAllPosts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = postAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun navigateToDetail(postId: Int) {
        val intent = Intent(requireActivity(), DetailPostActivity::class.java).apply {
            putExtra(EXTRA_POST_ID, postId)
        }
        startActivity(intent)
    }

    private fun showItemDeleteDialog(post: Post, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_post_dialog_title))
            .setMessage(getString(R.string.delete_post_dialog_message))
            .setPositiveButton(getString(R.string.title_delete)) { _, _ ->
                deletePost(post, position)
            }
            .setNegativeButton(getString(R.string.title_cancel), null)
            .show()
    }

    private fun deletePost(post: Post, position: Int) {
        viewModel.removePost(post)
        Snackbar.make(
            binding.root,
            getString(R.string.deleted_post_message),
            Snackbar.LENGTH_LONG
        ).show()
        postAdapter.notifyItemRemoved(position)
    }

    private fun setupListeners() = with(binding) {
        btnRetryAllPosts.setOnClickListener {
            viewModel.getAllPost(isRefreshing = false)
        }
        textDeleteAllPost.setOnClickListener {
            showDeleteAllPostDialog()
        }
        swipeToRefreshAllPosts.setOnRefreshListener {
            viewModel.getAllPost(isRefreshing = true)
            swipeToRefreshAllPosts.isRefreshing = false
        }
    }

    private fun showDeleteAllPostDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_all_post_dialog_title))
            .setMessage(getString(R.string.delete_all_post_dialog_message))
            .setPositiveButton(getString(R.string.title_delete)) { _, _ ->
                deleteAllPost()
            }
            .setNegativeButton(getString(R.string.title_cancel), null)
            .show()
    }

    private fun deleteAllPost() = with(binding) {
        viewModel.removeAllPost()
        Snackbar.make(
            root,
            getString(R.string.deleted_all_post_message),
            Snackbar.LENGTH_LONG
        ).show()
        postAdapter.notifyItemRangeRemoved(0, postAdapter.itemCount)
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
    }

    fun handleUiState(uiState: AllPostUiState) {
        binding.progressBarAllPosts.isVisible = uiState.isLoading

        val hasItems = uiState.items.isNotEmpty()
        binding.linearAllPost.isVisible = hasItems
        binding.linearAllPostEmpty.isVisible = !hasItems && !uiState.isLoading
        binding.textDeleteAllPost.isVisible = hasItems
        if (hasItems) {
            postAdapter.submitList(uiState.items)
            binding.textAllPostSize.text = String.format(
                getString(R.string.title_post_size),
                uiState.postSize
            )
        }

        if (uiState.errorMessage.isNotEmpty()) {
            Snackbar.make(binding.root, uiState.errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}