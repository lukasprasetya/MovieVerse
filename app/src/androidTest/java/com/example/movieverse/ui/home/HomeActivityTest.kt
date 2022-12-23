package com.example.movieverse.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.movieverse.R
import com.example.movieverse.utils.DataDummy
import com.example.movieverse.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeActivityTest {

    private val dummyMovie = DataDummy.generateDummyMovies()
    private val dummyTvShow = DataDummy.generareDummyTvShows()

    @Before
    fun setUp() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun loadMovie() {
        onView(withId(R.id.rv_movie_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie_fragm)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummyMovie.size
            )
        )
    }

    @Test
    fun loadDetailMovie() {
        onView(withId(R.id.rv_movie_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )

        onView(withId(R.id.tv_title_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_rate_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_desc_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_poster_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_header_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_release_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.collapseLayout)).perform(swipeUp())
    }

    @Test
    fun loadTvShow() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_tvshow_fragm)).apply {
            check(matches(isDisplayed()))
            perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyTvShow.size))
        }
    }

    @Test
    fun loadDetailTvShow() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_tvshow_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.tv_title_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_rate_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_desc_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_poster_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_header_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_release_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.collapseLayout)).perform(swipeUp())
    }

    @Test
    fun loadFavMovie() {
        onView(withId(R.id.action_fav)).perform(click())
        onView(withId(R.id.rv_favMovie_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favMovie_fragm)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummyMovie.size
            )
        )
    }

    @Test
    fun loadDetailFavMovie() {
        onView(withId(R.id.rv_movie_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )

        onView(withId(R.id.favorite_button)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.action_fav)).perform(click())
        onView(withId(R.id.rv_favMovie_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.tv_title_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_rate_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_desc_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_poster_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_header_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_release_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.favorite_button)).perform(click())

        onView(withId(R.id.collapseLayout)).perform(swipeUp())
    }

    @Test
    fun loadFavTvShow() {
        onView(withId(R.id.action_fav)).perform(click())
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_FavTvshow_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_FavTvshow_fragm)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummyTvShow.size
            )
        )
    }

    @Test
    fun loadDetailFavTvShow() {
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_tvshow_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_tvshow_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.favorite_button)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.action_fav)).perform(click())
        onView(withText("TV SHOWS")).perform(click())
        onView(withId(R.id.rv_FavTvshow_fragm)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_FavTvshow_fragm)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.tv_title_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_rate_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_desc_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_poster_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.img_header_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_release_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.favorite_button)).perform(click())

        onView(withId(R.id.collapseLayout)).perform(swipeUp())
    }
}