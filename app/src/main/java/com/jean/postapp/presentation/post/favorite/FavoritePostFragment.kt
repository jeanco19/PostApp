package com.jean.postapp.presentation.post.favorite

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
import com.jean.postapp.databinding.FragmentFavoritePostBinding
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.presentation.post.all.adapter.PostAdapter
import com.jean.postapp.presentation.post.favorite.state.FavoritePostUiState
import com.jean.postapp.presentation.post.favorite.viewmodel.FavoritePostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritePostFragment : Fragment(R.layout.fragment_favorite_post) {

    private var _binding: FragmentFavoritePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritePostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        collectUiState()
        viewModel.getFavoritePosts()
    }

    private fun setupRecycler() {
        postAdapter = PostAdapter(
            onClick = { },
            onDelete = { post, position ->
                showRemoveFavoriteDialog(post, position)
            }
        )
        binding.rvFavoritePosts.apply {
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

    private fun showRemoveFavoriteDialog(post: Post, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.remove_favorite_post_dialog_title))
            .setMessage(getString(R.string.remove_favorite_post_dialog_message))
            .setPositiveButton(getString(R.string.title_remove)) { _, _ ->
                removePost(post, position)
            }
            .setNegativeButton(getString(R.string.title_cancel), null)
            .show()
    }

    private fun removePost(post: Post, position: Int) {
        viewModel.removeFavorite(post)
        Snackbar.make(
            binding.root,
            getString(R.string.removed_favorite_post_message),
            Snackbar.LENGTH_LONG
        ).show()
        postAdapter.notifyItemRemoved(position)
        viewModel.getFavoritePosts()
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                handleUiState(uiState)
            }
        }
    }

    fun handleUiState(uiState: FavoritePostUiState) = with(binding) {
        progressBarFavorites.isVisible = uiState.isLoading

        val hasItems = uiState.items.isNotEmpty()
        rvFavoritePosts.isVisible = hasItems
        linearFavoritesEmpty.isVisible = !hasItems && !uiState.isLoading
        if (hasItems) postAdapter.submitList(uiState.items)

        if (uiState.errorMessage.isNotEmpty()) {
            Snackbar.make(root, uiState.errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}