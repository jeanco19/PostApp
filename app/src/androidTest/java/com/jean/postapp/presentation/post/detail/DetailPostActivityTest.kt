package com.jean.postapp.presentation.post.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.jean.postapp.R
import com.jean.postapp.presentation.post.detail.state.PostDetailUiState
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
class DetailPostActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(DetailPostActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun check_uiElementsStates_whenIsLoading() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.handleUiState(
                uiState = PostDetailUiState(
                    post = null,
                    isLoading = true,
                    errorMessage = "",
                    informativeMessage = ""
                )
            )
        }

        onView(withId(R.id.linear_detail))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_detail_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_post_detail))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun check_uiState_whenReceiveData() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.handleUiState(
                uiState = PostDetailUiState(
                    post = providePost(isSeen = true, isFavorite = true),
                    isLoading = false,
                    errorMessage = "",
                    informativeMessage = ""
                )
            )
        }

        onView(withId(R.id.linear_detail))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.linear_detail_empty))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress_bar_post_detail))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_uiState_whenReceiveError() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.handleUiState(
                uiState = PostDetailUiState(
                    post = null,
                    isLoading = false,
                    errorMessage = "Ha ocurrido un error al recuperar la información de la publicación.",
                    informativeMessage = ""
                )
            )
        }

        onView(withId(R.id.linear_detail))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.linear_detail_empty))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress_bar_post_detail))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun check_actionBar_whenInsertFavoritePost() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.handleUiState(
                uiState = PostDetailUiState(
                    post = providePost(isSeen = true, isFavorite = false),
                    isLoading = false,
                    errorMessage = "",
                    informativeMessage = ""
                )
            )
        }

        onView(withId(R.id.tv_author_name)).perform(click())
        onView(withId(R.id.icon_favorite)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Se agregó publicación a favoritas")))
    }

    @Test
    fun check_actionBar_whenRemoveFavoritePost() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.handleUiState(
                uiState = PostDetailUiState(
                    post = providePost(isSeen = true, isFavorite = true),
                    isLoading = false,
                    errorMessage = "",
                    informativeMessage = ""
                )
            )
        }

        onView(withId(R.id.tv_author_name)).perform(click())
        onView(withId(R.id.icon_favorite)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Se retiro publicación de favoritas")))
    }
}