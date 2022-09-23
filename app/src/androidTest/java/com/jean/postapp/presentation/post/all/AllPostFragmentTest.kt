package com.jean.postapp.presentation.post.all

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.jean.postapp.R
import com.jean.postapp.clickInChild
import com.jean.postapp.launchFragmentInHiltContainer
import com.jean.postapp.presentation.post.all.adapter.PostAdapter
import com.jean.postapp.presentation.post.all.state.AllPostUiState
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
class AllPostFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_uiState_whenIsLoading() {
        launchFragmentInHiltContainer<AllPostFragment> {
                handleUiState(
                    uiState = AllPostUiState(
                        items = emptyList(),
                        isLoading = true,
                        postSize = 0,
                        errorMessage = ""
                    )
                )
        }

        onView(withId(R.id.linear_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_all_post_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_all_posts))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.text_delete_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_uiState_whenReceiveData() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = listOf(providePost(isSeen = false, isFavorite = false)),
                    isLoading = false,
                    postSize = 1,
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.linear_all_post))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.linear_all_post_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_all_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.text_delete_all_post))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.text_all_post_size))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.text_all_post_size))
            .check(matches(withText("Publicaciones (1)")))
    }

    @Test
    fun check_uiState_whenDataIsEmpty() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = emptyList(),
                    isLoading = false,
                    postSize = 0,
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.linear_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_all_post_empty))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_bar_all_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.text_delete_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.text_all_post_size))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_uiState_whenReceiveError() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = emptyList(),
                    isLoading = false,
                    postSize = 0,
                    errorMessage = "Ha ocurrido un error inesperado."
                )
            )
        }

        onView(withId(R.id.linear_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_all_post_empty))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_bar_all_posts))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.text_delete_all_post))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.text_all_post_size))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Ha ocurrido un error inesperado.")))
    }

    @Test
    fun check_showDialog_whenClickedDeleteAllPostOption() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = listOf(providePost(isSeen = false, isFavorite = false)),
                    isLoading = false,
                    postSize = 1,
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.text_delete_all_post)).perform(click())
        onView(withText(R.string.delete_all_post_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun check_showDeleteDialog_whenClickedDeleteItemOption() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = listOf(providePost(isSeen = false, isFavorite = false)),
                    isLoading = false,
                    postSize = 1,
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_all_posts))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PostAdapter.PostViewHolder>(
                        0,
                        clickInChild(R.id.iv_remove)
                    )
            )
        onView(withText("¿Estás seguro que quieres eliminar esta publicación?"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun check_navigationToDetail_whenClickedItemList() {
        launchFragmentInHiltContainer<AllPostFragment> {
            handleUiState(
                uiState = AllPostUiState(
                    items = listOf(providePost(isSeen = false, isFavorite = false)),
                    isLoading = false,
                    postSize = 1,
                    errorMessage = ""
                )
            )
        }

        onView(withId(R.id.rv_all_posts))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PostAdapter.PostViewHolder>(
                        0,
                        clickInChild(R.id.card_item_post)
                    )
            )
        onView(withId(R.id.content_post_detail)).check(matches(isDisplayed()))
    }
}