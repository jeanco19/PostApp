package com.jean.postapp.presentation.post.favorite

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.jean.postapp.R
import com.jean.postapp.clickInChild
import com.jean.postapp.launchFragmentInHiltContainer
import com.jean.postapp.presentation.post.all.adapter.PostAdapter
import com.jean.postapp.presentation.post.favorite.state.FavoritePostUiState
import com.jean.postapp.providePost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class FavoritePostFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_uiState_whenIsLoading() {
        launchFragmentInHiltContainer<FavoritePostFragment> {
            handleUiState(
                uiState = FavoritePostUiState(
                    items = listOf(),
                    isLoading = true,
                    informativeMessage = "",
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_favorite_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_favorites_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_favorites))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun check_uiState_whenReceiveData() {
        launchFragmentInHiltContainer<FavoritePostFragment> {
            handleUiState(
                uiState = FavoritePostUiState(
                    items = listOf(providePost(isSeen = true, isFavorite = true)),
                    isLoading = false,
                    informativeMessage = "",
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_favorite_posts))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.linear_favorites_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_favorites))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_uiState_whenDataIsEmpty() {
        launchFragmentInHiltContainer<FavoritePostFragment> {
            handleUiState(
                uiState = FavoritePostUiState(
                    items = listOf(),
                    isLoading = false,
                    informativeMessage = "",
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_favorite_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_favorites_empty))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_bar_favorites))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_uiState_whenReceiveError() {
        launchFragmentInHiltContainer<FavoritePostFragment> {
            handleUiState(
                uiState = FavoritePostUiState(
                    items = listOf(),
                    isLoading = false,
                    informativeMessage = "",
                    errorMessage = "Ha ocurrido un error inesperado."
                )
            )
        }

        onView(withId(R.id.rv_favorite_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_favorites_empty))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_bar_favorites))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Ha ocurrido un error inesperado.")))
    }

    @Test
    fun check_showDialog_whenClickedRemoveItemOption() {
        launchFragmentInHiltContainer<FavoritePostFragment> {
            handleUiState(
                uiState = FavoritePostUiState(
                    items = listOf(providePost(isSeen = true, isFavorite = true)),
                    isLoading = false,
                    informativeMessage = "",
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_favorite_posts))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PostAdapter.PostViewHolder>(
                        0,
                        clickInChild(R.id.iv_remove)
                    )
            )
        onView(withText("¿Estás seguro que quieres retirar esta publicación de favoritas?"))
            .check(matches(isDisplayed()))
    }
}